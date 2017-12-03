package com.example.foodlife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class editRecipes extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "SavedRecipes";

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter descriptionAdapter;

    private ArrayList<String> allIngredients = new ArrayList<String>();
    private ArrayList<String> allDirections = new ArrayList<String>();
    JSONObject recipe = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipes);

        String title = (String) getIntent().getSerializableExtra("title");
        SharedPreferences sharedPref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String strRecipe = sharedPref.getString(title, "0");


        if(strRecipe != null) try {
            recipe = new JSONObject(strRecipe);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.ingredList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new InputListAdapterCopy(allIngredients);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView descriptionRecyclerView = (RecyclerView) findViewById(R.id.instructionsList);
        descriptionRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager descriptionLayoutManager = new LinearLayoutManager(this);
        descriptionRecyclerView.setLayoutManager(descriptionLayoutManager);
        descriptionAdapter = new InputListAdapter(allDirections);
        descriptionRecyclerView.setAdapter(descriptionAdapter);


        final EditText titleBar = (EditText)findViewById(R.id.Title);
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

        titleBar.setText(title);
        try{
            JSONArray extendedIngredients = (JSONArray) recipe.get("extendedIngredients");
            for(int i = 0; i < extendedIngredients.length(); i++){
                JSONObject currentIngredient = (JSONObject)extendedIngredients.get(i);
                System.out.println(currentIngredient.get("originalString"));
                allIngredients.add((String) currentIngredient.get("originalString"));
                mAdapter.notifyDataSetChanged();
            }
            if(recipe.get("instructions").equals(null)){
                instructionInput.setText("");
            }else{
                instructionInput.setText(recipe.get("instructions").toString());
            }

        }catch(JSONException e){
            e.printStackTrace();
        }



        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(titleBar.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Enter a Title!", Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        JSONObject result  = new JSONObject();
                        result.put("title", titleBar.getText().toString());
                        result.put("image", recipe.get("image"));
                        JSONArray extendedIngredients = new JSONArray();
                        for(String ingredient : allIngredients){
                            JSONObject originalString = new JSONObject();
                            originalString.put("originalString", ingredient);
                            extendedIngredients.put(originalString);
                        }
                        StringBuilder instructions = new StringBuilder();
                        for(String directions : allDirections){
                            instructions.append(directions).append("\n");
                        }

                        result.put("extendedIngredients", extendedIngredients);
                        result.put("instructions", instructions.toString());



                        System.out.println(result);

                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString(titleBar.getText().toString(), result.toString());
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), recipeBook.class);
                        startActivity(intent);
                        finish();


                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }

            }
        });

    }

}
