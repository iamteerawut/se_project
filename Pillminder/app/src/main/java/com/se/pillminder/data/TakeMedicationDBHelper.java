package com.se.pillminder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Japp on 12/11/2017.
 */

public class TakeMedicationDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "medication.db";
    private  static final int DATABASE_VERSION = 1;

    public TakeMedicationDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TAKE_MEDICATION_TABLE = "CREATE TABLE " +
                TakeMedicationContract.TakeMedicationEntry.TABLE_NAME + " (" +
                TakeMedicationContract.TakeMedicationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TakeMedicationContract.TakeMedicationEntry.COL_MED_ID + " INTEGER NOT NULL, " +
                TakeMedicationContract.TakeMedicationEntry.COL_MED_DATE + " TEXT NOT NULL, " +
                TakeMedicationContract.TakeMedicationEntry.COL_MED_CONSUMPTION + " TEXT NOT NULL, " +
                ");";
        // Execute query by calling execSQL on SQLiteDatabase db and pass the string to create the db
        db.execSQL(SQL_CREATE_TAKE_MEDICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
