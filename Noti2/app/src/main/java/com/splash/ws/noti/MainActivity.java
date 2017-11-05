package com.splash.ws.noti;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


//if not work try to change AppCompatActivity to ActionBarActivity or change ActionBarActivity to AppCompatActivity
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>HERE<<<<<<<<<<
public class MainActivity extends AppCompatActivity {

    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);


    }

    public void btnoti(View view){

        //Build The Notification

        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("TEST5555");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("HELLO");
        notification.setContentText("55555");


        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //Builds Notification and issue it.
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID,notification.build());


    }
}
