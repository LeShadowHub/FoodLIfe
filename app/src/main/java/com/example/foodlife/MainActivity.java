package com.example.foodlife;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView testing = (TextView) findViewById(R.id.testing);
        Button btnNote = (Button) findViewById(R.id.btnNote);
        ResizableImageView pantryView = (ResizableImageView) findViewById(R.id.btnPantry);
        ResizableImageView utilityView = (ResizableImageView) findViewById(R.id.btnUtility);
        ResizableImageView groceryView = (ResizableImageView) findViewById(R.id.btnGrocery);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
             user_id = extras.getString("USER_ID");
        }

        //testing.setText(user_id);

        pantryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPantryActivity = new Intent(MainActivity.this, PantryActivity.class);
                newPantryActivity.putExtra("USER_ID", user_id);
                startActivity(newPantryActivity);
            }
        });

        utilityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newUtilityActivity = new Intent(MainActivity.this, UtilityActivity.class);
                startActivity(newUtilityActivity);
            }
        });

        groceryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGroceryActivity = new Intent(MainActivity.this, GroceryActivity.class);
                startActivity(newGroceryActivity);
            }
        });

        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newNoteListActivity = new Intent(MainActivity.this, NoteListActivity.class);
                startActivity(newNoteListActivity);
            }
        });
    }
}
