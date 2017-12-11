package com.se.pillminder.data;

import android.provider.BaseColumns;

/**
 * Created by Japp on 12/11/2017.
 */

public class TakeMedicationContract {
    public static final class TakeMedicationEntry implements BaseColumns{
        public static final String TABLE_NAME = "takeMedication";
        public static final String COL_MED_ID = "medID";
        public static final String COL_MED_DATE = "medDate";
        public static final String COL_MED_CONSUMPTION = "consumption";

    }
}
