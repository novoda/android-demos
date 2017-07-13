package com.novoda.demo.tddpersistence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.novoda.demo.tddpersistence.db.TaskReaderDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.novoda.demo.tddpersistence.TaskNamedMatcher.aTaskNamed;
import static com.novoda.demo.tddpersistence.TaskRepositoryTest.TaskBuilder.aTask;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class TaskRepositoryTest {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
    private TaskRepository taskRepository;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        TaskReaderDbHelper dbHelper = new TaskReaderDbHelper(appContext);
        TaskDBStorage storage = new TaskDBStorage(dbHelper);
        TaskMapper mapper = new TaskMapper();

        taskRepository = new TaskRepository(mapper, storage);

        DatabaseCleaner cleaner = new DatabaseCleaner(dbHelper);
        cleaner.clean();
    }

    @Test
    public void findsExpiredTasks() throws Exception {
        String deadline = "2017-01-14";

        addTasks(
                aTask().withName("Task 1 (-Valid-)").withExpirationDate("2017-01-31"),
                aTask().withName("Task 2 (Expired)").withExpirationDate("2017-01-01"),
                aTask().withName("Task 3 (-Valid-)").withExpirationDate("2017-02-11"),
                aTask().withName("Task 4 (-Valid-)").withExpirationDate("2017-02-14"),
                aTask().withName("Task 5 (Expired)").withExpirationDate("2017-01-13")
        );

        assertTasksExpiringOn(deadline,
                containsInAnyOrder(
                        aTaskNamed("Task 2 (Expired)"),
                        aTaskNamed("Task 5 (Expired)"))
        );
    }

    private void addTasks(final TaskBuilder... tasks) throws Exception {
        for (TaskBuilder task : tasks) {
            taskRepository.persistTask(task.build());
        }
    }

    private void assertTasksExpiringOn(String deadline, Matcher<Iterable<? extends Task>> taskMatcher) throws ParseException {
        Date date = dateFormat.parse(deadline);
        assertThat(taskRepository.tasksExpiredBy(date), taskMatcher);
    }

    public static class TaskBuilder implements TestBuilder<Task> {

        private String name;
        private Date date;

        public static TaskBuilder aTask() {
            return new TaskBuilder();
        }

        public TaskBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public TaskBuilder withExpirationDate(String strDate) {

            try {
                date = dateFormat.parse(strDate);
            } catch (ParseException ex) {
                date = null;
            }

            return this;
        }

        @Override
        public Task build() {
            return new Task(name, date);
        }


    }
}
