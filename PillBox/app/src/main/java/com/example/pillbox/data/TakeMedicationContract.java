package com.example.pillbox.data;

import android.provider.BaseColumns;

/**
 * Created by luishengjie on 23/6/17.
 */

public class TakeMedicationContract {

    // Do not need to create _ID, BaseColumns does that automatically
    public static final class TakeMedicationEntry implements BaseColumns {
        // Create static final members for the table name and each of the db columns
        public static final String TABLE_NAME = "takeMedication";
        public static final String COL_MED_ID = "medId";
        public static final String COL_MED_DATE = "medDate";
        public static final String COL_MED_CONSUMPTION = "consumption";

    }

}
