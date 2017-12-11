package com.se.pillminder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Japp on 12/11/2017.
 */

public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medication.db";
    private  static final int DATABASE_VERSION = 1;

    public UserDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "database created");
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " +
                UserContract.UserEntry.TABLE_NAME + " (" +
                UserContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UserContract.UserEntry.COL_USERNAME + " TEXT NOT NULL" +
                ");";
        // Execute query by calling execSQL on SQLiteDatabase db and pass the string to create the db
        db.execSQL(SQL_CREATE_USER_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
