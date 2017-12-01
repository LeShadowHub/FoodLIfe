package com.example.foodlife;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.foodlife.R;
import com.example.foodlife.adapters.CustomCursorAdapter;
import com.example.foodlife.data.TaskContract;

public class GroceryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constants for logging and referring to unique loader
    private static final String TAG = GroceryActivity.class.getSimpleName();
    private static final int ITEM_LOADER_ID = 0;

    private CustomCursorAdapter mAdapter;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewItems);

        // Set layout for RecyclerView to be linear layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter and attach to the RecyclerView
        mAdapter = new CustomCursorAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        Toast.makeText(GroceryActivity.this, "Swipe Left or Right to DELETE", Toast.LENGTH_SHORT).show();

        /*
         * Add touch helper to RecyclerView to recognize when user swipes to delete an item.
         * ItemTouchHelper enables touch behavior (swipe and move) on each ViewHolder
         * uses callbacks to signal when user performs actions
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Retrieve id of item to delete
                int id = (int) viewHolder.itemView.getTag();

                // Build uri with String row id appended
                String stringId = Integer.toString(id);
                Uri uri = TaskContract.ItemEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                // Delete a single row of data using ContentResolver
                getContentResolver().delete(uri, null, null);

                // Restart the loader to re-query for all tasks after a deletion
                getSupportLoaderManager().restartLoader(ITEM_LOADER_ID, null, GroceryActivity.this);
            }
        }).attachToRecyclerView(mRecyclerView);

        /*
         * Set Floating action button to corresponding View
         * Attach OnClickListener to launch GrocerTaskActivity
         */
        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groceryTaskIntent = new Intent(GroceryActivity.this, GroceryTaskActivity.class);
                startActivity(groceryTaskIntent);
            }
        });

        /*
         * Ensure a loader is initialized and active, if loader doesn't exist, one is created, otherwise last created loader is re-used
         */
        getSupportLoaderManager().initLoader(ITEM_LOADER_ID, null, this);
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Re-query for all tasks
        getSupportLoaderManager().restartLoader(ITEM_LOADER_ID, null, this);
    }

    /**
     * Instantiates and returns a new AsyncTaskLoader with given ID
     * will return item data sa a Curosr or null if error
     */
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mItemData = null;

            // Called when loader first starts loading data
            @Override
            protected void onStartLoading(){
                if(mItemData != null){
                    deliverResult(mItemData);
                } else{
                    forceLoad();
                }
            }

            // Performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Query and load all task data in background, sort by priority

                try{
                    return getContentResolver().query(TaskContract.ItemEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                            TaskContract.ItemEntry.COLUMN_PRIORITY);
                } catch (Exception e){
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // sends result of he load, a Cursor, to registered listener
            public void deliverResult(Cursor data){
                mItemData = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Called when previously created loader has finished its load
     *
     * @param loader Loader that has finished
     * @param data data generated by the Loader
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update data that adapter uses to create ViewHolder
        mAdapter.swapCursor(data);

    }

    /**
     * Called when a previously created loader is being reset, making data
     * unavailable
     * Method removes any references this activity had to the loader's data
     *
     * @param loader Loader that is being reset
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
