package com.novoda.demo.tddpersistence;

import java.util.Date;

public class Task {
    private String name;
    private Date expiration;

    public Task(String name, Date expiration) {
        this.name = name;
        this.expiration = expiration;
    }

    public String getName() {
        return name;
    }

    public Date getExpiration() {
        return expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (name != null ? !name.equals(task.name) : task.name != null) return false;
        return expiration != null ? expiration.equals(task.expiration) : task.expiration == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (expiration != null ? expiration.hashCode() : 0);
        return result;
    }
}
