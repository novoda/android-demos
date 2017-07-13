package com.novoda.demo.tddpersistence;

import java.util.List;

public interface TaskStorage {
    void insert(TaskDBModel taskDBModel);

    void delete(TaskDBModel taskDBModel);

    List<TaskDBModel> findAllExpiredBy(long expirationDate);
    
    List<TaskDBModel> findAll();

    TaskDBModel findByName(String taskName);
}
