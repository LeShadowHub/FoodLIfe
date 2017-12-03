package com.example.foodlife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

public class pullRecipe extends AppCompatActivity {

    private static JSONObject recipe;
    ArrayList<String> originalStringIngredients = new ArrayList<String>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_recipe);

        Bundle b = getIntent().getExtras();
        int id = -1;
        if (b!= null) id = b.getInt("recipeID");
        getRecipeInformation(new VolleyCallbackJSONObject() {
            @Override
            public void onSuccess(JSONObject result) {
                recipe = result;
                getAllValues();
            }
        }, id);

        mRecyclerView = (RecyclerView) findViewById(R.id.ingredientList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new IngredientListAdapter(originalStringIngredients);
        mRecyclerView.setAdapter(mAdapter);
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

            instructions = (String)recipe.get("instructions");
            System.out.println(instructions);
            TextView instructionView = (TextView) findViewById(R.id.recipeDirections);
            instructionView.setText(instructions);
            readyInMinutes = recipe.get("readyInMinutes").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void getRecipeInformation(final VolleyCallbackJSONObject callback, int id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
