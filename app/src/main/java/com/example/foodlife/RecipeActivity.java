package com.example.foodlife;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class RecipeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //Add Recipe Button
        FloatingActionButton newRecipe = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        newRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecipe(view);
            }
        });

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



    }

    public void addRecipe(View view){
        Intent intent = new Intent(this, addRecipes.class);
        startActivity(intent);
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
                Intent intent = new Intent (this, recipeSuggestions.class);
                startActivity(intent);
                break;
            // action with ID action_settings was selected
            case R.id.search_button:
                Toast.makeText(this, "Enter your cravings here!", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



}
