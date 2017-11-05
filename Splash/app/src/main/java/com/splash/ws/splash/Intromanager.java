package com.splash.ws.splash;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aticom on 10/24/17.
 */

public class Intromanager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;


    public Intromanager(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences("first",0);
        editor = pref.edit();

    }

    public void setFirst(Boolean isFirst)
    {
        editor.putBoolean("check",isFirst);
        editor.commit();
    }

    public Boolean Check()
    {
        return pref.getBoolean("check",true);
    }



}
