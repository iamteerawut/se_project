package com.example.pillbox.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pillbox.Medication;
import com.example.pillbox.R;
import com.example.pillbox.adapter.DisplayMedsAdapter;
import com.example.pillbox.data.MedicationContract;
import com.example.pillbox.data.MedicationDbHelper;

import java.util.ArrayList;

/**
 * Created by luishengjie on 5/6/17.
 */

public class DisplayMedsFragment extends Fragment {
    // This method creates a new instance of the PillBoxFragment
    private SQLiteDatabase mMedListDb;

    public static DisplayMedsFragment newInstance(){
        DisplayMedsFragment fragment = new DisplayMedsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Create a UserListDbHelper instance, pass "this" to constructor as context
        MedicationDbHelper medicationDbHelper = new MedicationDbHelper(getActivity());
        // Get a writable database reference using getWritableDatabase and store it in mUserListDb
        mMedListDb = medicationDbHelper.getWritableDatabase();
        //insertFakeData(mMedListDb);
        Cursor cursor = getAllMeds();
        // Store Medication Objects
        ArrayList<Medication> medList = new ArrayList<Medication>();

        if (cursor.moveToFirst()){
            do{
                String medId =  Long.toString(cursor.getLong(cursor.getColumnIndex("_id")));
                String medName = cursor.getString(cursor.getColumnIndex("medName"));
                int dosage = cursor.getInt(cursor.getColumnIndex("dosage"));
                int morning = cursor.getInt(cursor.getColumnIndex("morning"));
                int afternoon = cursor.getInt(cursor.getColumnIndex("afternoon"));
                int night = cursor.getInt(cursor.getColumnIndex("night"));
                int evening = cursor.getInt(cursor.getColumnIndex("evening"));

                String beforeFood = cursor.getString(cursor.getColumnIndex("beforeFood"));
                int amount = cursor.getInt(cursor.getColumnIndex("amount"));
                String units = cursor.getString(cursor.getColumnIndex("units"));

                Medication medication = new Medication(medId, medName, dosage, morning, afternoon, night, evening, beforeFood, amount, units);
                medList.add(medication);

            }while(cursor.moveToNext());
        }
        cursor.close();
        mMedListDb.close();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_display_meds, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.med_display_recycler_view);
        recyclerView.setHasFixedSize(true);

        // Display medList data
        DisplayMedsAdapter adapter = new DisplayMedsAdapter(medList, getActivity());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        return rootView;
    }

    /**
     * @return all entry from the database medication
     * */
    private Cursor getAllMeds(){
        // Call query on mUserListDb in the table name order by COLUMN_TIMESTAMP
        return mMedListDb.query(
                MedicationContract.MedicationEntry.TABLE_NAME,
                null,       // Projection array: array of columns interested in calling
                null,
                null,
                null,
                null,
                MedicationContract.MedicationEntry.COL_MED_NAME
        );
    }
}

