package com.androidworks.familytreeadmin.data;

import android.provider.BaseColumns;

public class FamilyContract {

    private FamilyContract() {
    }

    /* Inner class that defines the table contents */
    public static class FamilyTable implements BaseColumns {
        public static final String TABLE_NAME = "family";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NICKNAME = "nickname";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_LOCATION = "location";
    }
}
