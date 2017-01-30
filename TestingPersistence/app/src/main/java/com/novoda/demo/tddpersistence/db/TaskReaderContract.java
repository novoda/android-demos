package com.novoda.demo.tddpersistence.db;

import android.provider.BaseColumns;

public final class TaskReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private TaskReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_NAME = "name";
        public static final String COLUMN_TASK_EXPIRATION = "expiration";
    }
}
