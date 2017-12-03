package com.example.foodlife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class pullRecipe extends AppCompatActivity{
    public static final String MY_PREFS_NAME = "SavedRecipes";
    private static JSONObject recipe;
    ArrayList<String> originalStringIngredients = new ArrayList<String>();
    int id = 0;
    SharedPreferences.Editor editor;

    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_recipe);

        Bundle b = getIntent().getExtras();
        if (b!= null) id = b.getInt("recipeID");
        getRecipeInformation(new VolleyCallbackJSONObject() {
            @Override
            public void onSuccess(JSONObject result) {
                recipe = result;
                System.out.println(recipe);
                getAllValues();
            }
        }, id);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.ingredientList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new IngredientListAdapter(originalStringIngredients);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton newRecipe = (FloatingActionButton) findViewById(R.id.favoriteFAB);
        newRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    editor = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

                    getRecipeInformation(new VolleyCallbackJSONObject() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            recipe = result;
                            try {
                                setPreferences(editor, (String) recipe.get("title"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, id);


                Toast.makeText(getApplicationContext(), "Added to recipe book!", Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void setPreferences(SharedPreferences.Editor editor, String title){
        editor.putString(title, recipe.toString());
        editor.apply();
    }
    private void getAllValues(){

        String title;
        String readyInMinutes;
        String imageURL;
        String instructions;

        try {
            JSONArray extendedIngredients = (JSONArray) recipe.get("extendedIngredients");
            for(int i = 0; i < extendedIngredients.length(); i++){
                JSONObject currentIngredient = (JSONObject)extendedIngredients.get(i);
                originalStringIngredients.add((String) currentIngredient.get("originalString"));
                mAdapter.notifyDataSetChanged();
            }


            title = (String)recipe.get("title");
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



    }

    private void getRecipeInformation(final VolleyCallbackJSONObject callback, int id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        System.out.println(id);
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"+id+"/information?includeNutrition=false";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-Mashape-Key", keys.getFoodAPIKey());
                return params;
            }
        };
        requestQueue.add(jsObjRequest);

    }


}

