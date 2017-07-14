package com.novoda.demo.tddpersistence;

import android.database.sqlite.SQLiteDatabase;

import com.novoda.demo.tddpersistence.db.TaskReaderContract;
import com.novoda.demo.tddpersistence.db.TaskReaderDbHelper;

import java.sql.SQLException;



public class DatabaseCleaner {

    private static final String[] TABLES = {
            // Add tables to delete here
            TaskReaderContract.TaskEntry.TABLE_NAME
    };

    private final TaskReaderDbHelper dbHelper;

    public DatabaseCleaner(TaskReaderDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void clean() throws SQLException {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        for (String table : TABLES) {
            sqLiteDatabase.delete(table, null, null);
        }

        dbHelper.close();
    }
}
