package com.novoda.demo.tddpersistence;

public class TaskDBModel {

    private final String name;
    private final long expiration;

    public TaskDBModel(String name, long expiration) {
        this.name = name;
        this.expiration = expiration;
    }

    public String getName() {
        return name;
    }

    public long getExpiration() {
        return expiration;
    }
}
