package com.example.pillbox.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pillbox.R;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private static String TIME_PREFERENCES = "timePreference";
    public static final String MORNING = "morning";
    public static final String AFTERNOON = "afternoon";
    public static final String NIGHT = "night";
    public static final String EVENING = "evening";

    public static final String DEFAULT_MORNING = "08:00";
    public static final String DEFAULT_AFTERNOON = "12:00";
    public static final String DEFAULT_EVENING = "18:00";
    public static final String DEFAULT_NIGHT = "21:00";

    private SharedPreferences sharedpreferences;

    private EditText mSetMorning;
    private EditText mSetAfternoon;
    private EditText mSetEvening;
    private EditText mSetNight;

    private TextView mToolbarTitle;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedpreferences = getSharedPreferences(TIME_PREFERENCES, Context.MODE_PRIVATE);

        mToolbarTitle = (TextView)findViewById(R.id.toolbar_title);
        mToolbarTitle.setText("Settings");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSetMorning = (EditText)findViewById(R.id.set_morning);
        mSetAfternoon = (EditText)findViewById(R.id.set_afternoon);
        mSetEvening = (EditText)findViewById(R.id.set_evening);
        mSetNight = (EditText)findViewById(R.id.set_night);


        updateTextLabels();
        mSetMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String morning = sharedpreferences.getString(MORNING, DEFAULT_MORNING);
                updateTimeInput(morning, MORNING);
            }
        });

        mSetAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String afternoon = sharedpreferences.getString(AFTERNOON, DEFAULT_AFTERNOON);
                updateTimeInput(afternoon, AFTERNOON);
            }
        });

        mSetEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String evening = sharedpreferences.getString(EVENING, DEFAULT_EVENING);
                updateTimeInput(evening ,EVENING);
            }
        });

        mSetNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String night = sharedpreferences.getString(NIGHT, DEFAULT_NIGHT);
                updateTimeInput(night ,NIGHT);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            setResult(RESULT_OK);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Takes in a String that represents time in 24-hour and updates the timing
     * */
    private void updateTimeInput(String time ,String type){
        Log.d(TAG, "TIME IS " + time);
        int hours = getHours(time);
        int mins = getMins(time);
        updateTime(hours, mins,type);
    }

    /**
     * Takes in a String that represents time in 24-hour and returns
     * the Hours
     * */
    private int getHours(String time){
        Integer hours;
        if(isNumeric(time.substring(1,2))){
            hours = Integer.valueOf(time.substring(0,2));
        }else{
            hours = Integer.valueOf(time.substring(0,1));
        }

        return hours;
    }

    /**
     * Takes in a String that represents time in 24-hour and returns
     * the Minutes
     * */
    private int getMins(String time){
        Integer minutes;

        if(isNumeric(time.substring(1,2))){
            minutes = Integer.valueOf(time.substring(3));
        }else{
            minutes = Integer.valueOf(time.substring(2));
        }
        return minutes;
    }

    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }


    private void updateTextLabels(){
        // Morning
        String morning = sharedpreferences.getString(MORNING, DEFAULT_MORNING);
        //String morningInAM = convertTimeToAMPM(morning);
        mSetMorning.setText(morning);
        // Afternoon
        String afternoon = sharedpreferences.getString(AFTERNOON, DEFAULT_AFTERNOON);
        //String afternoonInPM = convertTimeToAMPM(afternoon);
        mSetAfternoon.setText(afternoon);
        // Evening
        String evening = sharedpreferences.getString(EVENING, DEFAULT_EVENING);
        //String eveningInPM = convertTimeToAMPM(evening);
        mSetEvening.setText(evening);
        // Night
        String night = sharedpreferences.getString(NIGHT, DEFAULT_NIGHT);
        //String nightInPM = convertTimeToAMPM(night);
        mSetNight.setText(night);

    }

    private void updateTime(int hours, int mins, String type){
        TimePickerDialog.OnTimeSetListener time = null;
        switch(type){
            case MORNING:
                time = timeMorning;
                break;
            case AFTERNOON:
                time = timeAfternoon;
                break;
            case EVENING:
                time = timeEvening;
                break;
            case NIGHT:
                time = timeNight;
                break;
        }
        new TimePickerDialog(this, time, hours, mins, true).show();
    }

    TimePickerDialog.OnTimeSetListener timeMorning = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = getTimingInFormat(hourOfDay, minute);
            // Update SharedPreference
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(MORNING, time).apply();
            editor.commit();
            updateTextLabels();
        }
    };

    TimePickerDialog.OnTimeSetListener timeAfternoon = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = getTimingInFormat(hourOfDay, minute);
            // Update SharedPreference
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(AFTERNOON, time).apply();
            editor.commit();
            updateTextLabels();
        }
    };

    TimePickerDialog.OnTimeSetListener timeEvening = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = getTimingInFormat(hourOfDay, minute);
            // Update SharedPreference
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(EVENING, time).apply();
            editor.commit();
            updateTextLabels();
        }
    };

    TimePickerDialog.OnTimeSetListener timeNight = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = getTimingInFormat(hourOfDay, minute);
            // Update SharedPreference
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(NIGHT, time).apply();
            editor.commit();
            updateTextLabels();
        }
    };

    private String getTimingInFormat(int hourOfDay, int minute){
        String hourOfDayStr = Integer.toString(hourOfDay);
        String minuteStr = Integer.toString(minute);
        if(hourOfDay < 10){
            hourOfDayStr = "0" + hourOfDayStr;
        }
        if(minute < 10){
            minuteStr = "0" + minuteStr;
        }
        return hourOfDayStr + ":" + minuteStr;
    }

}
