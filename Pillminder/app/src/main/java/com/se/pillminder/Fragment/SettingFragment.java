package com.se.pillminder.Fragment;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;

import com.se.pillminder.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private static final String TAG = "SettingFragment";

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



    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedpreferences = getActivity().getSharedPreferences(TIME_PREFERENCES, Context.MODE_PRIVATE);


        mSetMorning = (EditText)view.findViewById(R.id.set_morning);
        mSetAfternoon = (EditText)view.findViewById(R.id.set_afternoon);
        mSetEvening = (EditText)view.findViewById(R.id.set_evening);
        mSetNight = (EditText)view.findViewById(R.id.set_night);

        mSetMorning.setInputType(InputType.TYPE_NULL);
        mSetAfternoon.setInputType(InputType.TYPE_NULL);
        mSetEvening.setInputType(InputType.TYPE_NULL);
        mSetNight.setInputType(InputType.TYPE_NULL);

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

    private void updateTimeInput(String time , String type){
        Log.d(TAG, "TIME IS " + time);
        int hours = getHours(time);
        int mins = getMins(time);
        updateTime(hours, mins,type);
    }

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
        new TimePickerDialog(this.getContext(), time, hours, mins, true).show();
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
