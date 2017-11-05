package com.se.pillminder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_pill:
                    //setTitle("Pill Page");
                    PillPage fragmentPill = new PillPage();
                    FragmentTransaction fragmentTransactionPill = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionPill.replace(R.id.content, fragmentPill, "FragmentName");
                    fragmentTransactionPill.commit();
                    return true;
                case R.id.navigation_alarm:
                    //setTitle("Alert Page");
                    AlertPage fragmentAlert = new AlertPage();
                    FragmentTransaction fragmentTransactionAlert = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionAlert.replace(R.id.content, fragmentAlert, "FragmentName");
                    fragmentTransactionAlert.commit();
                    return true;
                case R.id.navigation_setting:
                    //setTitle("Setting Page");
                    SettingPage fragmentSetting = new SettingPage();
                    FragmentTransaction fragmentTransactionSetting = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionSetting.replace(R.id.content, fragmentSetting, "FragmentName");
                    fragmentTransactionSetting.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //setTitle("Pill Page");
        PillPage fragment = new PillPage();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "FragmentName");
        fragmentTransaction.commit();

    }

}
