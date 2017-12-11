package com.se.pillminder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Japp on 12/11/2017.
 */

public class MedicationDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medication.db";
    private static final int DATABASE_VERSION = 1;

    public MedicationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_Create_MEDICATION_Table = "CREATE TABLE"+
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

                MedicationContract.MedicationEntry.COL_USER_ID + " TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_Create_MEDICATION_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MedicationContract.MedicationEntry.TABLE_NAME);
        onCreate(db);

    }
}
