package com.splash.ws.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Page2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_2);

        final Intent k = new Intent(this, Page2.class);

        Thread timerrr = new Thread()
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
                    startActivity(k);
                    finish();
                }

            }

        };
        timerrr.start();
    }
}