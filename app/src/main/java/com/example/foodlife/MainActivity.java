package com.example.foodlife;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGrocery = (Button) findViewById(R.id.btnNote);
        ResizableImageView pantryView = (ResizableImageView) findViewById(R.id.btnPantry);
        ResizableImageView utilityView = (ResizableImageView) findViewById(R.id.btnUtility);

        pantryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPantryActivity = new Intent(MainActivity.this, PantryActivity.class);
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

        btnGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newNoteListActivity = new Intent(MainActivity.this, NoteListActivity.class);
                startActivity(newNoteListActivity);

            }
        });
    }
}
