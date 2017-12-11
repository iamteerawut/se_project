package com.se.pillminder.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.se.pillminder.Fragment.AlertFragment;
import com.se.pillminder.Fragment.HistoryFragment;
import com.se.pillminder.Fragment.PillFragment;
import com.se.pillminder.R;
import com.se.pillminder.Fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

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
