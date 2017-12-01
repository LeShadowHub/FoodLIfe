package com.example.foodlife.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Created by OEM on 11/30/2017.
 */

public class TaskContentProvider extends ContentProvider{

    // Final constants for directory of items and single item
    public static final int ITEMS = 100;
    public static final int ITEM_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Associates URI's with their int matches
     */
    public static UriMatcher buildUriMatcher(){

        // Initialize UriMatcher with no matches
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add matches for the item directory and a single item by ID
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_ITEMS, ITEMS);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_ITEMS + "/#", ITEM_WITH_ID);

        return uriMatcher;
    }

    private TaskDbHelper mTaskDbHelper;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mTaskDbHelper = new TaskDbHelper(context);
        return true;
    }

    // Handle requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Get access to item database
        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for items directory
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case ITEMS:
                // Insert new values into database
                long id = db.insert(TaskContract.ItemEntry.TABLE_NAME, null, values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(TaskContract.ItemEntry.CONTENT_URI, id);
                } else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            // Set the value for returnUri and write default case for unkown URI's
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify resolver if uri has changed, and return newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri
        return returnUri;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Get access to database
        final SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();

        // Write URI match code and set variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for items directory
        switch (match){
            case ITEMS:
                retCursor = db.query(TaskContract.ItemEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set notification URI on Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int itemsDeleted;

        switch (match){
            // Single item case, recognized by ID in URI path
            case ITEM_WITH_ID:
                String id = uri.getPathSegments().get(1);
                itemsDeleted = db.delete(TaskContract.ItemEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }

        // Notify resolver and return num of items deleted
        if(itemsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return itemsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
