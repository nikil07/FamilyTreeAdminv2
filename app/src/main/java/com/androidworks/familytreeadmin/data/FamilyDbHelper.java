package com.androidworks.familytreeadmin.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FamilyDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Family.db";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + FamilyContract.FamilyTable.TABLE_NAME + " ( " +
                    FamilyContract.FamilyTable._ID + " INTEGER PRIMARY KEY," +
                    FamilyContract.FamilyTable.COLUMN_NAME      + " TEXT," +
                    FamilyContract.FamilyTable.COLUMN_NICKNAME  + " TEXT," +
                    FamilyContract.FamilyTable.COLUMN_AGE       + " INTEGER," +
                    FamilyContract.FamilyTable.COLUMN_LOCATION  + " TEXT" +
                    ");";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FamilyContract.FamilyTable.TABLE_NAME;
    
    public FamilyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
