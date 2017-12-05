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
            "Notes"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        setTitle("Utilities");

        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        circleMenu.setMainMenu(Color.parseColor("#795548"), R.drawable.ic_plus_white_36dp, R.drawable.ic_cancel_white_36dp)
                .addSubMenu(Color.parseColor("#29d69f"), R.drawable.ic_weight_kilogram_white_48dp)  //Conversions
                .addSubMenu(Color.parseColor("#20ab7f"), R.drawable.ic_alarm_white_48dp)   //Timer
                .addSubMenu(Color.parseColor("#188160"), R.drawable.ic_note_text_white_48dp)  //Quick Tips
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        Toast.makeText(UtilityActivity.this, "You selected " + select[index], Toast.LENGTH_SHORT).show();

                        Runnable conversions = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(UtilityActivity.this, ConversionsActivity.class);
                                startActivity(intent);
                            }
                        };

                        Runnable timer = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(UtilityActivity.this, TimerActivity.class);
                                startActivity(intent);
                            }
                        };

                        Runnable tips = new Runnable() {
                            @Override
                            public void run() {
                                // if you are redirecting from a fragment then use getActivity() as the context.
                                Intent intent = new Intent(UtilityActivity.this, NoteListActivity.class);
                                startActivity(intent);
                                // To close the CurrentActitity, r.g. SpalshActivity
                            }
                        };

                        if(index == 0){
                            Handler h = new Handler();
                            h.postDelayed(conversions, 1500);
                        }
                        if(index == 1){
                            Handler h = new Handler();
                            h.postDelayed(timer, 1500);
                        }

                        if(index == 2){
                            Handler h = new Handler();
                            h.postDelayed(tips, 1500);
                        }
                    }
                })
                .openMenu();
    }
}