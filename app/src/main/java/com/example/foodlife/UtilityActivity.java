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

    String select[] = {"grocery",
            "pantry",
            "utility"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.ic_note_add_white_36dp, R.drawable.ic_cancel_white_36dp)
                .addSubMenu(Color.parseColor("#258CFF"), R.drawable.grocery_title)
                .addSubMenu(Color.parseColor("#6D4C41"), R.drawable.pantry_title)
                .addSubMenu(Color.parseColor("#FF0000"), R.drawable.utility_title)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        Toast.makeText(UtilityActivity.this, "You selected " + select[index], Toast.LENGTH_SHORT).show();

                        /*new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent i=new Intent(UtilityActivity.this, NoteActivity.class);
                                startActivity(i);
                            }
                        }, 5000);*/

                        /*Runnable r = new Runnable() {
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
                            // The Runnable will be executed after the given delay time
                            h.postDelayed(r, 1000); // will be delayed for 1.5 seconds
                        }*/
                    }
                })
                .openMenu();
    }
}
