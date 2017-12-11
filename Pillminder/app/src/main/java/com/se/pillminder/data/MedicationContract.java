package com.se.pillminder.data;

import android.provider.BaseColumns;

/**
 * Created by Japp on 12/11/2017.
 */

public class MedicationContract {
    public static final class MedicationEntry implements BaseColumns{
        public static final String TABLE_NAME = "medication";
        public static final String COL_MED_NAME = "medName";
        public static final String COL_DOSAGE = "dosage";
        public static final String COL_FREQ = "frequency";
        public static final String COL_BEFORE_FOOD = "beforeFood";
        public static final String COL_AMOUNT = "amount";
        public static final String COL_UNITS = "unit";
        public static final String COL_START_DATE = "startDate";
        public static final String COL_END_DATE = "endDate";

        public static final String COL_MORNING = "morning";
        public static final String COL_AFTERNOON = "afternoon";
        public static final String COL_EVENING = "evening";
        public static final String COL_NIGHT = "night";
        public static final String COL_USER_ID = "userID";

    }
}
