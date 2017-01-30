package com.novoda.demo.tddpersistence;

import java.util.Date;


public class TaskMapper {

    public TaskDBModel fromDomain(Task task) {
        Date expiration = task.getExpiration();
        if (expiration == null) {
            expiration = new Date();
        }
        return new TaskDBModel(
                task.getName(),
                expiration.getTime()
        );
    }

    public Task toDomain(TaskDBModel taskDBModel) {
        return new Task(
                taskDBModel.getName(),
                new Date(taskDBModel.getExpiration())
        );
    }

}
