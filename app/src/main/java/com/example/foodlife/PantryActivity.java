package com.example.foodlife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {

    private ArrayList<String> ingredients = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
    }

    public ArrayList<String> getIngredients(){
        return ingredients;
    }
}
