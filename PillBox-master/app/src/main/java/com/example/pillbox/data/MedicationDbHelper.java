package com.example.pillbox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicationDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medication.db";
    // Store current database version
    // Increment every time database schema modified
    private  static final int DATABASE_VERSION = 1;

    public MedicationDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MEDICATION_TABLE = "CREATE TABLE " +
                MedicationContract.MedicationEntry.TABLE_NAME + " (" +
                MedicationContract.MedicationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MedicationContract.MedicationEntry.COL_MED_NAME + " TEXT NOT NULL, " +
                MedicationContract.MedicationEntry.COL_DOSAGE + " INTEGER NOT NULL, " +
                MedicationContract.MedicationEntry.COL_MORNING + " INTEGER NOT NULL, " +
                MedicationContract.MedicationEntry.COL_AFTERNOON + " INTEGER NOT NULL, " +
                MedicationContract.MedicationEntry.COL_EVENING + " INTEGER NOT NULL, " +
                MedicationContract.MedicationEntry.COL_NIGHT + " INTEGER NOT NULL, " +

                MedicationContract.MedicationEntry.COL_BEFORE_FOOD + " TEXT NOT NULL, " +
                MedicationContract.MedicationEntry.COL_AMOUNT + " INTEGER NOT NULL, " +
                MedicationContract.MedicationEntry.COL_UNITS + " TEXT NOT NULL, " +
                MedicationContract.MedicationEntry.COL_START_DATE + " TEXT NOT NULL, " +
                MedicationContract.MedicationEntry.COL_END_DATE + " TEXT NOT NULL " +
                ");";
        // Execute query by calling execSQL on SQLiteDatabase db and pass the string to create the db
        db.execSQL(SQL_CREATE_MEDICATION_TABLE);
    }

    // Override onUpgrade method
    // Method needs to up upgraded if more columns are to be added
    // append column to db insted of deleting and recreating
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MedicationContract.MedicationEntry.TABLE_NAME);
        onCreate(db);
    }

}
