package com.example.pillbox.data;

import android.provider.BaseColumns;

/**
 * Created by luishengjie on 5/6/17.
 */

public class MedicationContract {
    // Create an inner class MedicationEntry that implements the BaseColumns interface
    // Do not need to create _ID, BaseColumns does that automatically
    public static final class MedicationEntry implements BaseColumns {
        // Create static final members for the table name and each of the db columns
        public static final String TABLE_NAME = "medication";
        public static final String COL_MED_NAME = "medName";
        public static final String COL_DOSAGE = "dosage";
//        public static final String COL_FREQ = "frequency";
        public static final String COL_BEFORE_FOOD = "beforeFood";
        public static final String COL_AMOUNT = "amount";
        public static final String COL_UNITS = "units";
        public static final String COL_START_DATE = "startDate";
        public static final String COL_END_DATE = "endDate";

        public static final String COL_MORNING = "morning";
        public static final String COL_AFTERNOON = "afternoon";
        public static final String COL_EVENING = "evening";
        public static final String COL_NIGHT = "night";

    }
}
