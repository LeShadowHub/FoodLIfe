package com.example.foodlife;

import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private static ArrayList<String> pantryIngredients = new ArrayList<String>();
    private static ArrayList <JSONObject> returnRecipes = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        pantryIngredients = (ArrayList<String>) getIntent().getSerializableExtra("pantry");


        //Custom App Bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Navigation Menu Button on App Bar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        generateRecipes(new VolleyCallbackJSONArray() {
            @Override
            public void onSuccess(JSONArray result) {
                try{
                    for (int j = 0; j < result.length(); j++) {
                        JSONObject recipe = result.getJSONObject(j);
                        returnRecipes.add(recipe);
                    }
                    show();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private void show(){
        //Intent intent = new Intent(this, RecipeListView.class);
        //intent.putExtra("recipes", returnRecipes);
        //startActivity(intent);

        mRecyclerView = (RecyclerView) findViewById(R.id.recipeList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecipeListAdapter(returnRecipes);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void generateRecipes(final VolleyCallbackJSONArray callback){

        String urlInput = "";
        if(pantryIngredients != null){
            if(pantryIngredients.size() != 0){
                urlInput = pantryIngredients.get(0);
                for(int i = 1; i < pantryIngredients.size(); i++){
                    urlInput += "%2C" + pantryIngredients.get(i);
                }
            }
        }



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients= "
                + urlInput +"&limitLicense=false&number=1&ranking=1";

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.nav_Groceries){
            Intent intent = new Intent(this, GroceryActivity.class);
            startActivity(intent);
        }
        if(id == R.id.nav_Pantry){
            Intent intent = new Intent(this, PantryActivity.class);
            startActivity(intent);
        }
        if(id == R.id.nav_Recipes){
            Toast.makeText(this, "You are currently on Recipes.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, recipeBook.class);
            startActivity(intent);
        }
        if(id == R.id.nav_Utilities){
            Intent intent = new Intent(this, ConversionsActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipes_appbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.suggestions_button:
                Toast.makeText(this, "Suggestions based on your pantry.", Toast.LENGTH_SHORT)
                        .show();


                break;
            // action with ID action_settings was selected
            case R.id.search_button:
                Toast.makeText(this, "Search by Ingredients.", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent (this, recipeSuggestionsInput.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



}
