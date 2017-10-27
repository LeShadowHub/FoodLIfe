package com.example.foodlife;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class RecipeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //Floating add button- opens new activity
        FloatingActionButton newRecipe = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        newRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecipe(view);
            }
        });

        //navigation menu
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

        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return mToggle.onOptionsItemSelected(item)|| super.onOptionsItemSelected(item);
    }

    public void addRecipe(View view){
        Snackbar.make(view, "Add Recipe", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Intent intent = new Intent(this, addRecipes.class);
        startActivity(intent);
    }
}
