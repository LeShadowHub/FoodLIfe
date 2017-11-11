package com.example.foodlife;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class recipeSuggestions extends AppCompatActivity {

    String ingredients;
    ArrayList <String> allIngredients = new ArrayList<String>();
  //  ArrayAdapter<String> allIngredients;

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
        mAdapter = new MyAdapter(allIngredients);
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

    private String generateRecipes(){


        return null;}
}


