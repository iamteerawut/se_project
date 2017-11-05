package com.splash.ws.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_1);

        final Intent i = new Intent(this, Page2.class);

        Thread timerr = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(5000);
                }

                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                finally
                {
                    startActivity(i);
                }

            }

        };
        timerr.start();



    }
}
