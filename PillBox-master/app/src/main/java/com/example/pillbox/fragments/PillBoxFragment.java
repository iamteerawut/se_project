package com.example.pillbox.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.pillbox.R;
import com.example.pillbox.TakeMedication;
import com.example.pillbox.adapter.TakeMedsAdapter;
import com.example.pillbox.data.MedicationDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Displays the Medications to be taken today
 *
 * Created by luishengjie on 5/6/17.
 */

public class PillBoxFragment extends Fragment {
    private SQLiteDatabase mMedListDb;
    private ArrayList<TakeMedication> mTakeMedList;

    private TextView mDateTextView;
    Calendar dateTime = Calendar.getInstance();

    /**
     * Creates a new instance of PillBoxFragment
     * */
    public static PillBoxFragment newInstance(){
        PillBoxFragment fragment = new PillBoxFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * @return all entry from the database medication
     * */
    private Cursor getMedsForToday(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDate = dateFormat.format(dateTime.getTime());

        Cursor cursor = mMedListDb.rawQuery("SELECT * " +
                        "FROM medication " +
                        "WHERE " +
                        "startDate <= " + "'"+currDate+ "'"
                        + " AND " + "endDate >= " + "'"+currDate+ "'" , null);
        return cursor;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Compare current time with settings time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        Log.d("TIME", str);
        // Get medication table values of medications taken today
        // Create a UserListDbHelper instance, pass "this" to constructor as context
        MedicationDbHelper medicationDbHelper = new MedicationDbHelper(getActivity());
        // Get a writable database reference using getWritableDatabase and store it in mUserListDb
        mMedListDb = medicationDbHelper.getWritableDatabase();
        ArrayList<TakeMedication> mTakeMedMorningList = new ArrayList<>();
        ArrayList<TakeMedication> mTakeMedAfternoonList = new ArrayList<>();
        ArrayList<TakeMedication> mTakeMedEveningList = new ArrayList<>();
        ArrayList<TakeMedication> mTakeMedNightList = new ArrayList<>();

        Cursor cursor = getMedsForToday();
        if (cursor.moveToFirst()){
            do{
                int medId =  (int)cursor.getLong(cursor.getColumnIndex("_id"));
                String medName = cursor.getString(cursor.getColumnIndex("medName"));
                int dosage = cursor.getInt(cursor.getColumnIndex("dosage"));
                int morning = cursor.getInt(cursor.getColumnIndex("morning"));
                int afternoon = cursor.getInt(cursor.getColumnIndex("afternoon"));
                int night = cursor.getInt(cursor.getColumnIndex("night"));
                int evening = cursor.getInt(cursor.getColumnIndex("evening"));
                String beforeFood = cursor.getString(cursor.getColumnIndex("beforeFood"));

                if(morning != 0){
                    String timeOfDay = "morning";
                    TakeMedication medication = new TakeMedication(medId, medName, timeOfDay, dosage, beforeFood);
                    mTakeMedMorningList.add(medication);
                }
                if(afternoon != 0){
                    String timeOfDay = "afternoon";
                    TakeMedication medication = new TakeMedication(medId, medName, timeOfDay, dosage, beforeFood);
                    mTakeMedAfternoonList.add(medication);
                }
                if(evening != 0){
                    String timeOfDay = "evening";
                    TakeMedication medication = new TakeMedication(medId, medName, timeOfDay, dosage, beforeFood);
                    mTakeMedEveningList.add(medication);
                }
                if(night != 0){
                    String timeOfDay = "night";
                    TakeMedication medication = new TakeMedication(medId, medName, timeOfDay, dosage, beforeFood);
                    mTakeMedNightList.add(medication);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        mMedListDb.close();
        mTakeMedList = new ArrayList<>();
        mTakeMedList.addAll(mTakeMedMorningList);
        mTakeMedList.addAll(mTakeMedAfternoonList);
        mTakeMedList.addAll(mTakeMedEveningList);
        mTakeMedList.addAll(mTakeMedNightList);

        View rootView = inflateLayout(inflater, container);
        return rootView;
    }

    private View inflateLayout(LayoutInflater inflater, ViewGroup container){
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pillbox, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.med_display_recycler_view);
        recyclerView.setHasFixedSize(true);
        // Display medList data
        TakeMedsAdapter adapter = new TakeMedsAdapter(mTakeMedList, getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Update TextView
        mDateTextView = (TextView) rootView.findViewById(R.id.date_tv);
        updateDateLabel();
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });
        return rootView;
    }

    private void updateDate(){
        new DatePickerDialog(getActivity() , datePickerDialog ,dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateTime.set(year, month,dayOfMonth);
            updateDateLabel();
        }
    };

    public void updateDateLabel(){
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(dateTime.getTime());
        mDateTextView.setText(formattedDate);
    }

}
