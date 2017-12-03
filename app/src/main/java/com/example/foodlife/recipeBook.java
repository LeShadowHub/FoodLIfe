package com.example.foodlife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class recipeBook extends AppCompatActivity {
    private static final String MY_PREFS_NAME = "SavedRecipes";

    private ArrayList<JSONObject> recipeList = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_book);

        FloatingActionButton newRecipe = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        newRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), addRecipes.class);
                startActivity(intent);
            }
        });

        //pull recipes from internal storage (JSONArray), store in recipeList
        SharedPreferences myPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allRecipes = myPreferences.getAll();
        for(Map.Entry<String, ?> entry : allRecipes.entrySet()){
            try {
                recipeList.add(new JSONObject((String)entry.getValue()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recipeList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new RecipeBookAdapter(recipeList);
        mRecyclerView.setAdapter(mAdapter);

    }
}
