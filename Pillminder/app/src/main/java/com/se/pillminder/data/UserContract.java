package com.se.pillminder.data;

import android.provider.BaseColumns;

/**
 * Created by Japp on 12/11/2017.
 */

public class UserContract {
    public static final class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "user";
        public static final String COL_USERNAME = "username";
    }
}
