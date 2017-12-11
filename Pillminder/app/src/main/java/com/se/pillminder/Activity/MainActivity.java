package com.se.pillminder.Activity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.se.pillminder.Fragment.AlertFragment;
import com.se.pillminder.Fragment.HistoryFragment;
import com.se.pillminder.Fragment.PillFragment;
import com.se.pillminder.R;
import com.se.pillminder.Fragment.SettingFragment;
import com.se.pillminder.data.MedicationContract;
import com.se.pillminder.data.MedicationDBHelper;
import com.se.pillminder.data.TakeMedicationDBHelper;
import com.se.pillminder.data.UserContract;
import com.se.pillminder.data.UserDBHelper;

public class MainActivity extends AppCompatActivity {

    MedicationDBHelper medicationDBHelper;

    public MedicationDBHelper getMedicationDBHelper() {
        return medicationDBHelper;
    }

    public TakeMedicationDBHelper getTakeMedicationDBHelper() {
        return takeMedicationDBHelper;
    }

    public UserDBHelper getUserDBHelper() {
        return userDBHelper;
    }

    TakeMedicationDBHelper takeMedicationDBHelper;
    UserDBHelper userDBHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_pill:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, PillFragment.newInstance()).commit();
                    return true;
                case R.id.navigation_alert:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, AlertFragment.newInstance()).commit();
                    return true;
                case R.id.navigation_history:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HistoryFragment.newInstance()).commit();
                    return true;
                case R.id.navigation_setting:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SettingFragment.newInstance()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isFirstRun();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, PillFragment.newInstance()).commit();
        Log.i("TAG", "start to create database");
        userDBHelper =  new UserDBHelper(getApplicationContext());
        medicationDBHelper = new MedicationDBHelper(getApplicationContext());
        takeMedicationDBHelper = new TakeMedicationDBHelper(getApplicationContext());

//        UserDBHelper userDBHelper = new UserDBHelper(getApplicationContext());
//        MedicationDBHelper medicationDBHelper = new MedicationDBHelper(getApplicationContext());
//
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(UserContract.UserEntry.COL_USERNAME, "testUser");
//        userDBHelper.getWritableDatabase().insert(UserContract.UserEntry.TABLE_NAME, null, contentValues);
//
//        String[] columns =  {UserContract.UserEntry._ID
//                , UserContract.UserEntry.COL_USERNAME};
//
//        Cursor cursor = userDBHelper.getReadableDatabase().query(UserContract.UserEntry.TABLE_NAME
//                , columns
//                , null
//                , null
//                , null
//                , null
//                ,  UserContract.UserEntry.COL_USERNAME+" DESC");
//
//        while(cursor.moveToNext()){
//            Log.i("DB TEST", cursor.getString(1));
//        }
//        Log.i("Db", "insert data in to database");

    }

    private void isFirstRun() {
        final String PREFS_NAME = "AppPreferences";
        final String KEY_FIRSTRUN = "FirstRun";

        SharedPreferences sp;
        SharedPreferences.Editor editor;

        sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sp.edit();

        boolean isFirstRun = sp.getBoolean(KEY_FIRSTRUN, false);
        editor.putBoolean(KEY_FIRSTRUN, true);
        editor.commit();

    }

}
