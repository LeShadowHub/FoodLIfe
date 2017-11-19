package com.example.foodlife;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class recipeSuggestions extends AppCompatActivity {

    String ingredients;
    ArrayList <String> allIngredients = new ArrayList<String>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_suggestions);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Adapter(allIngredients);
        mRecyclerView.setAdapter(mAdapter);


        final EditText ingredientInput = (EditText) findViewById(R.id.ingredientInput);

        final Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
               allIngredients.add(ingredientInput.getText().toString());
               mAdapter.notifyDataSetChanged();
               ingredientInput.getText().clear();
            }
        });

        final Button goButton = (Button) findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                generateRecipes();
            }
        });



    }

    private ArrayList<JSONObject> generateRecipes(){

        final ArrayList <JSONObject> returnRecipes = new ArrayList<JSONObject>();

        allIngredients = Adapter.getValues();
        String urlInput = allIngredients.get(0);
        for(int i = 1; i < allIngredients.size(); i++){
            urlInput += "%2C" + allIngredients.get(i);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients= "
                + urlInput +"&limitLicense=false&number=10&ranking=1";

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int j = 0; j < response.length(); j++) {
                        JSONObject recipe = response.getJSONObject(j);
                        returnRecipes.add(recipe);
                    


                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
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

        return returnRecipes;}

}


