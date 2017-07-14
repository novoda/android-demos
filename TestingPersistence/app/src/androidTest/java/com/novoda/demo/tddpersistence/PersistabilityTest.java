package com.novoda.demo.tddpersistence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.novoda.demo.tddpersistence.db.TaskReaderDbHelper;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.novoda.demo.tddpersistence.TaskRepositoryTest.TaskBuilder.aTask;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class PersistabilityTest {
    private TaskRepository taskRepository;

    List<TestBuilder<Task>> persistentObjectBuilders = Arrays.<TestBuilder<Task>>asList(
            aTask().withName("A task").withExpirationDate("2017-03-16"),
            aTask().withName("A task (with date in the past)").withExpirationDate("2017-01-01")

    );
    private TaskDBStorage storage;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        TaskReaderDbHelper dbHelper = new TaskReaderDbHelper(appContext);
        storage = new TaskDBStorage(dbHelper);
        TaskMapper mapper = new TaskMapper();

        taskRepository = new TaskRepository(mapper, storage);

        DatabaseCleaner cleaner = new DatabaseCleaner(dbHelper);
        cleaner.clean();

    }

    @Test
    public void roundTripsPersistentObjects() {
        for (TestBuilder builder : persistentObjectBuilders) {
            assertCanBePersisted(builder);
        }
    }

    private void assertCanBePersisted(TestBuilder<Task> builder) {
        assertReloadsWithSameStateAs(persistedObjectFrom(builder));
    }

    private void assertReloadsWithSameStateAs(Task original) {
        Task savedTask = taskRepository.taskWithName(original.getName());

        // Without reflection this can fail:
        // If a new field is added to the object without including it on the equals method and it is wrongly mapped this matcher won't pick it up.
        assertThat(savedTask, equalTo(original));
    }

    private Task persistedObjectFrom(TestBuilder<Task> builder) {
        Task original = builder.build();

        taskRepository.persistTask(original);
        return original;
    }

}
