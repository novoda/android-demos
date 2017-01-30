package com.novoda.demo.tddpersistence;


public class MainActivityPresenter {

    private final TaskRepository taskRepository;

    public MainActivityPresenter(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void save(Task task) {
        taskRepository.persistTask(task);
    }

}
