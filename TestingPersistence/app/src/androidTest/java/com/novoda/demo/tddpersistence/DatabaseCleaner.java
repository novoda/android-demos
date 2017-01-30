package xyz.lgvalle.tddpersistence;

import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import xyz.lgvalle.tddpersistence.db.TaskReaderDbHelper;

import static xyz.lgvalle.tddpersistence.db.TaskReaderContract.*;

public class DatabaseCleaner {

    private static final String[] TABLES = {
            // Add tables to delete here
            TaskEntry.TABLE_NAME
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
