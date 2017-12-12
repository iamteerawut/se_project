package com.example.pillbox.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pillbox.R;
import com.example.pillbox.data.TakeMedicationContract;
import com.example.pillbox.data.TakeMedicationDbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TakeMedActivity extends AppCompatActivity {
    Button take_Medication;
    Button skip_Medication;
    private SQLiteDatabase mTakeListDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_med);
        TakeMedicationDbHelper takeMedicationDbHelper = new TakeMedicationDbHelper(this);
        mTakeListDb = takeMedicationDbHelper.getWritableDatabase();
        take_Medication = (Button) findViewById(R.id.take_medication);
        skip_Medication = (Button) findViewById(R.id.skip_medication);

    }
    public void take_Medicatio(View view){
        TakeMedicationDbHelper takeMedicationDbHelper = new TakeMedicationDbHelper(TakeMedActivity.this);
        List<ContentValues> list = new ArrayList<ContentValues>();

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String takeDate = dateFormat.format(calendar.getTime());

        ContentValues cv = new ContentValues();
        cv.put(TakeMedicationContract.TakeMedicationEntry.COL_MED_DATE, takeDate);

    }
}
