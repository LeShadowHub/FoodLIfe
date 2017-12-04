package com.example.foodlife;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.foodlife.data.TaskContract;

public class GroceryTaskActivity extends AppCompatActivity {

    private int mPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_task);

        setTitle("Add New Item");

        // Initialize highest mPriority by default
        ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
        mPriority = 1;
    }

    /**
     * Called when "ADD" button is clicked
     * Retrieves user input and inserts new item data into database
     * @param view
     */
    public void onClickAddItem(View view){
        // If EditText not empty, retrieve input and store in a ContentValues object
        String input = ((EditText) findViewById(R.id.etItemDescription)).getText().toString();
        if(input.length() == 0){
            return;
        }

        // Insert new item data via ContentResolver
        // Create new ContentValues object
        ContentValues contentValues = new ContentValues();

        // Put item desc and mPriority into ContentValues
        contentValues.put(TaskContract.ItemEntry.COLUMN_DESCRIPTION, input);
        contentValues.put(TaskContract.ItemEntry.COLUMN_PRIORITY, mPriority);

        // Insert content values via ContentResolver
        Uri uri = getContentResolver().insert(TaskContract.ItemEntry.CONTENT_URI, contentValues);

        /*if(uri != null){
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }*/

        finish();
    }

    /**
     * Called whenever a priority button is clicked
     * Changes value of mPriority based on selected
     */
    public void onPrioritySelected(View view){
        if(((RadioButton) findViewById(R.id.radButton1)).isChecked()){
            mPriority = 1;
        } else if(((RadioButton) findViewById(R.id.radButton2)).isChecked()){
            mPriority = 2;
        } else if(((RadioButton) findViewById(R.id.radButton3)).isChecked()){
            mPriority = 3;
        }
    }
}
