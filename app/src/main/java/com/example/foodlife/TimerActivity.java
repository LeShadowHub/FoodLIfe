package com.example.foodlife;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class TimerActivity extends AppCompatActivity {

    private CountDownTimer cdt;
    private TextView hourDisplay;
    private TextView minuteDisplay;
    private TextView secondDisplay;
    private long time;
    private EditText hour;
    private EditText min;
    private EditText sec;
    private Button startPauseBTN;
    private int h;
    private int m;
    private int s;
    boolean timerRunning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        hour = (EditText) findViewById(R.id.inHours);
        min = (EditText) findViewById(R.id.inMinutes);
        sec = (EditText) findViewById(R.id.inSeconds);

        hourDisplay = (TextView) findViewById(R.id.hrDisplay);
        minuteDisplay = (TextView) findViewById(R.id.minDisplay);
        secondDisplay = (TextView) findViewById(R.id.secDisplay);

        startPauseBTN = (Button) findViewById(R.id.button);

        timerRunning = false;

        InputFilter.LengthFilter lf = new InputFilter.LengthFilter(2);

        hour.setText("00");
        hour.setFilters(new InputFilter[] {lf});
        min.setText("00");
        min.setFilters(new InputFilter[] {lf});
        sec.setText("00");
        sec.setFilters(new InputFilter[] {lf});

        startPauseBTN.setText("start");
        startPauseBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!timerRunning) {
                    if (TextUtils.isEmpty(hour.getText().toString())) {
                        h = 0;
                    } else {
                        h = Integer.parseInt(hour.getText().toString());
                    }
                    if (TextUtils.isEmpty(min.getText().toString())) {
                        m = 0;
                    } else {
                        m = Integer.parseInt(min.getText().toString());
                    }
                    if (TextUtils.isEmpty(sec.getText().toString())) {
                        s = 0;
                    } else {
                        s = Integer.parseInt(sec.getText().toString());
                    }
                    if ((h + m + s) == 0) {
                        //Do Nothing
                    } else {
                        time = 3600000*h + 60000*m + 1000*s;
                        startPauseHandler();
                    }
                }
                else{
                    startPauseHandler();
                }
            }
        });

        Button reset = (Button) findViewById(R.id.resetBTN);
        reset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                resetButtonHandler();
            }
        });

        Button back = (Button) findViewById(R.id.backBTN);
        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                backButtonHandler();
            }
        });
    }

    public void startPauseHandler(){
        if (timerRunning) {
            pauseHelper();

        } else {
            startHelper();


        }
    }

    public void startHelper(){
        printDisplayTime();
        timerRunning = true;
        cdt = new CountDownTimer(time, 1000){
            @Override
            public void onTick(long holdTime){
                time = holdTime;
                printDisplayTime();
            }

            @Override
            public void onFinish(){
                //TODO Alarm should ring
            }
        }.start();
        startPauseBTN.setText("pause");
    }

    public void pauseHelper() {
        cdt.cancel();
        timerRunning = false;
        printInputTime();
        startPauseBTN.setText("start");
    }

    public void resetButtonHandler(){
        if(timerRunning){
            cdt.cancel();
            timerRunning = false;
            startPauseBTN.setText("start");
        }
        h = 0;
        m = 0;
        s = 0;
        time = 0;
        hourDisplay.setText(String.format("%02d",h));
        minuteDisplay.setText(String.format("%02d",m));
        secondDisplay.setText(String.format("%02d",s));
        hour.setText(String.format("%02d",h));
        min.setText(String.format("%02d",m));
        sec.setText(String.format("%02d",s));
        printInputTime();
    }

    public void backButtonHandler(){
        //TODO...
    }

    public void printDisplayTime(){
        s = (int)time/1000;
        m = (int)s/60;
        s = s%60;
        h = m/60;
        m = m%60;

        hourDisplay.setText(String.format("%02d",h));
        minuteDisplay.setText(String.format("%02d",m));
        secondDisplay.setText(String.format("%02d",s));
        hour.setText(String.format("%02d",h));
        min.setText(String.format("%02d",m));
        sec.setText(String.format("%02d",s));

        if(!timerRunning) {
            InputINVISIBLE();
            DisplayVISIBLE();
        }

    }

    public void printInputTime(){
        DisplayINVISIBLE();
        InputVISIBLE();
    }

    public void DisplayVISIBLE(){
        hourDisplay.setVisibility(View.VISIBLE);
        minuteDisplay.setVisibility(View.VISIBLE);
        secondDisplay.setVisibility(View.VISIBLE);
    }

    public void DisplayINVISIBLE(){
        hourDisplay.setVisibility(View.INVISIBLE);
        minuteDisplay.setVisibility(View.INVISIBLE);
        secondDisplay.setVisibility(View.INVISIBLE);
    }

    public void InputVISIBLE(){
        hour.setVisibility(View.VISIBLE);
        min.setVisibility(View.VISIBLE);
        sec.setVisibility(View.VISIBLE);
    }

    public void InputINVISIBLE(){
        hour.setVisibility(View.INVISIBLE);
        min.setVisibility(View.INVISIBLE);
        sec.setVisibility(View.INVISIBLE);
    }

}