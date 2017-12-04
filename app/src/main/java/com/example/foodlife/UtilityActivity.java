package com.example.foodlife;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class UtilityActivity extends AppCompatActivity {

    String select[] = {"Conversions",
            "Timer",
            "Quick Tips"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.ic_note_add_white_36dp, R.drawable.ic_cancel_white_36dp)
                .addSubMenu(Color.parseColor("#258CFF"), R.drawable.grocery_title)  //Conversions
                .addSubMenu(Color.parseColor("#6D4C41"), R.drawable.pantry_title)   //Timer
                .addSubMenu(Color.parseColor("#FF0000"), R.drawable.utility_title)  //Quick Tips
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        Toast.makeText(UtilityActivity.this, "You selected " + select[index], Toast.LENGTH_SHORT).show();

                        Runnable conversions = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(UtilityActivity.this, ConversionsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        };

                        Runnable timer = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(UtilityActivity.this, TimerActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        };

                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                // if you are redirecting from a fragment then use getActivity() as the context.
                                Intent intent = new Intent(UtilityActivity.this, NoteActivity.class);
                                startActivity(intent);
                                // To close the CurrentActitity, r.g. SpalshActivity
                                finish();
                            }
                        };

                        if(index == 0){
                            Handler h = new Handler();
                            h.postDelayed(conversions, 1000);
                        }
                        if(index == 1){
                            Handler h = new Handler();
                            h.postDelayed(timer, 1000);
                        }

                        if(index == 2){
                            Handler h = new Handler();
                            h.postDelayed(r, 1000);
                        }
                    }
                })
                .openMenu();
    }
}