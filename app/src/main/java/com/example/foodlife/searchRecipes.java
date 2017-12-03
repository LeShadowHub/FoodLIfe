package com.example.foodlife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class searchRecipes extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;

    ArrayList<JSONObject> recipeList = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);

        final EditText input = (EditText) findViewById(R.id.input);
        input.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                input.getText().clear();
            }
        });

        final Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recipeList.clear();
                generateRecipes(new VolleyCallbackJSONObject() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        System.out.println(result.toString());
                        try {
                            JSONArray list = (JSONArray) result.get("results");

                            for (int j = 0; j < list.length(); j++) {
                                JSONObject recipe = list.getJSONObject(j);
                                recipeList.add(recipe);
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            }
                    }
                 });
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recipes);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchRecipeAdapter(recipeList);
        mRecyclerView.setAdapter(mAdapter);

    }



    private void generateRecipes(final VolleyCallbackJSONObject callback){

        EditText input = (EditText)findViewById(R.id.input);
        String query = input.getText().toString();


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url =     "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?instructionsRequired=false&limitLicense=false&number=25&offset=0&query="+
                query +"&type=main+course";

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
