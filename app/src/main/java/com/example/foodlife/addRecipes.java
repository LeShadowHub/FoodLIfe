package com.example.foodlife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class addRecipes extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView descriptionRecyclerView;
    private RecyclerView.Adapter descriptionAdapter;
    private RecyclerView.LayoutManager descriptionLayoutManager;

    private ArrayList<String> allIngredients = new ArrayList<String>();
    private ArrayList<String> allDirections = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipes);

        mRecyclerView = (RecyclerView) findViewById(R.id.ingredList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new IngredientListAdapter(allIngredients);
        mRecyclerView.setAdapter(mAdapter);

        descriptionRecyclerView = (RecyclerView) findViewById(R.id.instructionsList);
        descriptionRecyclerView.setHasFixedSize(true);
        descriptionLayoutManager = new LinearLayoutManager(this);
        descriptionRecyclerView.setLayoutManager(descriptionLayoutManager);
        descriptionAdapter = new InputListAdapter(allDirections);
        descriptionRecyclerView.setAdapter(descriptionAdapter);


        final EditText title = (EditText)findViewById(R.id.Title);
        final EditText ingredientInput = (EditText)findViewById(R.id.ingredientInput);


        Button ingredientsButton = (Button) findViewById(R.id.enterIngredient);
        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                allIngredients.add(ingredientInput.getText().toString());
                mAdapter.notifyDataSetChanged();
                ingredientInput.getText().clear();

            }
        });

        final EditText instructionInput = (EditText)findViewById(R.id.instructionInput);
        final Button directionsButton = (Button) findViewById(R.id.enterDirections);
           directionsButton.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v){
                allDirections.add(instructionInput.getText().toString());
                descriptionAdapter.notifyDataSetChanged();
                instructionInput.getText().clear();
              }
      });

        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(title.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Enter a Title!", Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        JSONObject result  = new JSONObject();
                        result.put("title", title.getText().toString());
                        result.put("image", "null");
                        JSONArray extendedIngredients = new JSONArray();
                        for(String ingredient : allIngredients){
                            extendedIngredients.put(ingredient);
                        }
                        String instructions = "";
                        for(String directions : allDirections){
                            instructions += (directions + "\n");
                        }

                        result.put("extendedIngredients", extendedIngredients);
                        result.put("instructions", instructions);

                        System.out.println(result);


                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }

            }
        });

//on save button click, cast from json to string and save
        //how to make a json in java

    }

}