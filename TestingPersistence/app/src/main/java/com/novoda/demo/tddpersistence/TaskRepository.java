package com.novoda.demo.tddpersistence;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskRepository {
    private final TaskMapper taskMapper;
    private final TaskStorage taskDBStorage;

    public TaskRepository(TaskMapper taskMapper, TaskStorage taskDBStorage) {
        this.taskMapper = taskMapper;
        this.taskDBStorage = taskDBStorage;
    }

    public void persistTask(Task task) {
        TaskDBModel taskDBModel = taskMapper.fromDomain(task);
        taskDBStorage.insert(taskDBModel);
    }

    public List<Task> tasksExpiredBy(Date date) {
        long expirationDate = date.getTime();
        return dbTasksToDomain(taskDBStorage.findAllExpiredBy(expirationDate));
    }

    public Task taskWithName(String taskName) {
        TaskDBModel taskDBModel = taskDBStorage.findByName(taskName);
        return taskMapper.toDomain(taskDBModel);
    }

    public List<Task> allTasks() {
        return dbTasksToDomain(taskDBStorage.findAll());
    }

    @NonNull
    private List<Task> dbTasksToDomain(List<TaskDBModel> allExpiredBy) {
        List<Task> tasks = new ArrayList<>();
        for (TaskDBModel taskDBModel : allExpiredBy) {
            tasks.add(taskMapper.toDomain(taskDBModel));
        }
        return tasks;
    }
}
