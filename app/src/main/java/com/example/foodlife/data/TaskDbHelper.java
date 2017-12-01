package com.example.foodlife.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.foodlife.data.TaskContract.ItemEntry;

/**
 * Created by OEM on 11/30/2017.
 */

public class TaskDbHelper extends SQLiteOpenHelper{

    // Name of database
    private static final String DATABASE_NAME = "itemsDb.db";

    private static final int VERSION = 1;

    // Constructor
    TaskDbHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create items table
        final String CREATE_TABLE = "CREATE TABLE "  + ItemEntry.TABLE_NAME + " (" +
                        ItemEntry._ID                + " INTEGER PRIMARY KEY, " +
                        ItemEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        ItemEntry.COLUMN_PRIORITY    + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    // Only occurs when the version number for database is incremented
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
        onCreate(db);
    }
}
