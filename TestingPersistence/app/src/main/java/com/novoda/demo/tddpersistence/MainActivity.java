package com.novoda.demo.tddpersistence;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novoda.demo.tddpersistence.db.TaskReaderDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

    private TaskRepository taskRepository;
    private EditText taskEditTitle;
    private EditText taskEditDate;
    private LinearLayout tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskRepository = getRepository();
        initLayout();
    }

    private void initLayout() {
        findViewById(R.id.task_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddTask();
                refreshUI();
            }
        });

        taskEditTitle = ((EditText) findViewById(R.id.task_edit_title));
        taskEditDate = ((EditText) findViewById(R.id.task_edit_date));
        tasksList = (LinearLayout) findViewById(R.id.task_list);

        updateTaskList();
    }

    private void onAddTask() {
        Task task = createTask();
        taskRepository.persistTask(task);
    }

    private Task createTask() {
        String strDate = taskEditDate.getText().toString();

        Date date;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException ex) {
            date = null;
            Log.e(TAG, "A problem occur during the parsing of the string strDate: " + strDate, ex);
        }

        return new Task(taskEditTitle.getText().toString(), date);
    }

    private void refreshUI() {
        updateTaskList();
        taskEditTitle.setText("");
        taskEditDate.setText("");
    }

    private void updateTaskList() {
        tasksList.removeAllViews();

        List<Task> tasks = taskRepository.allTasks();
        for (Task task : tasks) {

            View taskLayout = LayoutInflater.from(this).inflate(R.layout.task, null);
            TextView taskTitle = (TextView) taskLayout.findViewById(R.id.task_title);
            TextView taskDate = (TextView) taskLayout.findViewById(R.id.task_date);

            taskTitle.setText(task.getName());
            taskDate.setText(dateFormat.format(task.getExpiration()));

            tasksList.addView(taskLayout);
        }
    }

    private TaskRepository getRepository() {
        TaskReaderDbHelper taskReaderDbHelper = new TaskReaderDbHelper(this);
        TaskStorage taskStorage = new TaskDBStorage(taskReaderDbHelper);
        TaskMapper taskMapper = new TaskMapper();

        return new TaskRepository(taskMapper, taskStorage);
    }
}
