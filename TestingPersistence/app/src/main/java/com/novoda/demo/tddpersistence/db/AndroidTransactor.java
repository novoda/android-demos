package com.novoda.demo.tddpersistence.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class AndroidTransactor {

    private static final String TAG = AndroidTransactor.class.getSimpleName();

    public interface UnitOfWork {
        void work(SQLiteDatabase db) throws Exception;
    }


    private final TaskReaderDbHelper dbHelper;

    public AndroidTransactor(TaskReaderDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void perform(UnitOfWork unitOfWork)  {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            unitOfWork.work(db);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }
        db.close();
    }

}
