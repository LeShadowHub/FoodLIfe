package com.example.foodlife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class displayRecipeBook extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "SavedRecipes";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    JSONObject recipe = null;

    ArrayList<String> originalStringIngredients = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_recipe);

        mRecyclerView = (RecyclerView) findViewById(R.id.ingredientList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new IngredientListAdapter(originalStringIngredients);
        mRecyclerView.setAdapter(mAdapter);

        String title = (String) getIntent().getSerializableExtra("title");
        SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String strRecipe = sharedPref.getString(title, "0");


        if(strRecipe != null) try {
            recipe = new JSONObject(strRecipe);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String imageURL;
        String instructions;
        String readyInMinutes;

        try {
            JSONArray extendedIngredients = (JSONArray) recipe.get("extendedIngredients");
            for(int i = 0; i < extendedIngredients.length(); i++){
                JSONObject currentIngredient = (JSONObject)extendedIngredients.get(i);
                System.out.println(currentIngredient.get("originalString"));
                originalStringIngredients.add((String) currentIngredient.get("originalString"));
                mAdapter.notifyDataSetChanged();
            }

            TextView titleView = (TextView) findViewById(R.id.recipeTitle);
            titleView.setText(title);

            ImageView recipeImage = (ImageView) findViewById(R.id.recipePicture);
            imageURL = (String)recipe.get("image");

            if(imageURL.contains("null")){
                recipeImage.setImageResource(R.drawable.recipe_pic_missing);
            }else Picasso.with(recipeImage.getContext()).load(imageURL).into(recipeImage);

            if(recipe.get("instructions").equals(null)){
                instructions = "No instructions provided.";
            }else{
                instructions = (String)recipe.get("instructions");
            }

            TextView instructionView = (TextView) findViewById(R.id.recipeDirections);
            instructionView.setText(instructions);
            readyInMinutes = recipe.get("readyInMinutes").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FloatingActionButton newRecipe = (FloatingActionButton) findViewById(R.id.favoriteFAB);
        newRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), editRecipes.class);
                try {
                    intent.putExtra("title", recipe.get("title").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });


    }
}
