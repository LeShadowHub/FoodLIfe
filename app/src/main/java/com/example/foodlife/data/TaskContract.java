package com.example.foodlife.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by OEM on 11/30/2017.
 */

public class TaskContract {


    /*
     * Add content provider constants to Contract
     * 1) Content authority,
     * 2) Base content URI,
     * 3) Path(s) to the tasks directory
     * 4) Content URI for data in the TaskEntry class
     */

    // authority, how code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.foodlife.provider";

    // base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Path for the "tasks" directory
    public static final String PATH_ITEMS = "items";

    // Inner class that defines contents of the item table
    public static final class ItemEntry implements BaseColumns{

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEMS).build();

        // item table and column names
        public static final String TABLE_NAME = "items";

        // Additional columns to "_ID"
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRIORITY = "priority";

         /*
            Table structure looks something like table below.
            With the name of the table and columns on top, and potential contents in rows
            Note: Because this implements BaseColumns, the _id column is generated automatically
            tasks
             - - - - - - - - - - - - - - - - - - - - - -
            | _id  |    description     |    priority   |
             - - - - - - - - - - - - - - - - - - - - - -
            |  1   |  Complete lesson   |       1       |
             - - - - - - - - - - - - - - - - - - - - - -
            |  2   |    Go shopping     |       3       |
             - - - - - - - - - - - - - - - - - - - - - -
            .
            .
            .
             - - - - - - - - - - - - - - - - - - - - - -
            | 43   |   Learn guitar     |       2       |
             - - - - - - - - - - - - - - - - - - - - - -
         */
    }
}
