package com.example.pillbox.activity;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pillbox.R;
import com.example.pillbox.adapter.NotificationPublisher;
import com.example.pillbox.data.MedicationContract;
import com.example.pillbox.data.MedicationDbHelper;
import com.example.pillbox.model.DisplayMedsFragment;
import com.example.pillbox.model.PillBoxFragment;
import com.example.pillbox.model.ReportFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static  final int EAT_MEDS_NOTI_ID = 99;
    private static final String TAG = "MainActivity";
    private SQLiteDatabase mMedListDb;
    private Toolbar mToolbar;
    private String mTagName;
    private CoordinatorLayout coordinatorLayout;

    NotificationManager notificationManager;

    boolean isNotificationActive = false;
    int notificationId = EAT_MEDS_NOTI_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference CoordinatorLayout
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        // Create a UserListDbHelper instance, pass "this" to constructor as context
        MedicationDbHelper medicationDbHelper = new MedicationDbHelper(this);
        // Get a writable database reference using getWritableDatabase and store it in mUserListDb
        mMedListDb = medicationDbHelper.getWritableDatabase();

        Cursor cursor = getAllMeds();
        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("medName"));
                Log.d(TAG, data + " :)");
            }while(cursor.moveToNext());
        }
        cursor.close();

        MedicationDbHelper medicationDBHelper = new MedicationDbHelper(getApplicationContext());
//      Test DB
        String[] columns =  {MedicationContract.MedicationEntry._ID
                , MedicationContract.MedicationEntry.COL_MED_NAME, MedicationContract.MedicationEntry.COL_AMOUNT,
                MedicationContract.MedicationEntry.COL_START_DATE, MedicationContract.MedicationEntry.COL_END_DATE, MedicationContract.MedicationEntry.COL_MORNING,
                MedicationContract.MedicationEntry.COL_DOSAGE};

        Cursor cursor2 = medicationDbHelper.getReadableDatabase().query(MedicationContract.MedicationEntry.TABLE_NAME
                , columns
                , null
                , null
                , null
                , null
                ,  MedicationContract.MedicationEntry.COL_MED_NAME+" DESC");

        while(cursor2.moveToNext()){
            Log.i("DB TEST", cursor2.getString(0)+cursor2.getString(1)+cursor2.getString(2)+cursor2.getString(3)
                    +cursor2.getString(4)+cursor2.getString(5)+cursor2.getString(6));
        }
        Log.i("Db", "insert data in to database");
//

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Replace Icon
        final Drawable settings = getResources().getDrawable(R.drawable.ic_settings_white);
        getSupportActionBar().setHomeAsUpIndicator(settings);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {
                            case R.id.action_pillbox:
                                selectedFragment = PillBoxFragment.newInstance();
                                mTagName = "action_pillbox";
                                break;
                            case R.id.action_display_meds:
                                selectedFragment = DisplayMedsFragment.newInstance();
                                mTagName = "action_display_meds";
                                break;
                            case R.id.action_report:
                                selectedFragment = ReportFragment.newInstance();
                                mTagName = "action_report";
                                break;
                        }
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment, mTagName);
                        transaction.commit();
                        return true;
                    }
                });


        // Manually displaying the first fragment (To be display when app first open)
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, PillBoxFragment.newInstance());
        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_navigation_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //No Inspection, SimplifiableIfStatement
        if(id == android.R.id.home) {
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(settings,1);
            return true;

        }  else if (id == R.id.action_add_med) {
            Intent add_med = new Intent(MainActivity.this,AddMedActivity.class);
            add_med.putExtra("PREV_ACTIVITY",TAG);
            startActivity(add_med);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(mTagName == null){
                return;
            }

            Fragment currentFragment = getFragmentManager().findFragmentByTag(mTagName);
            FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
        }
    }

    /**
     * This method returns all the medical entry from the database
     * */
    private Cursor getAllMeds(){
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

    public void setAlarm(View view){
        scheduleNotification(this, 2000,1);
    }

    public void scheduleNotification(Context context, long delay, int notificationId) {//delay is after how much time(in millis) from current time you want to schedule the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("TEST")
                .setContentText("TESTOMG")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, TakeMedActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Set the alarm to start at approximately xx:xx
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);

        // if the scheduler date is passed, move scheduler time to tomorrow
        if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }




}
