package com.example.foodlife.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodlife.R;
import com.example.foodlife.VolleyCallbackJSONObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.foodlife.keys;
import com.example.foodlife.pullRecipe;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    public static final String MY_PREFS_NAME = "SavedRecipes";
    private static ArrayList<JSONObject> values;
    private Context context;
    private JSONObject pulledRecipe;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public JSONObject currentItem;
        SharedPreferences.Editor editor;

        CardView cv;
        TextView recipeTitle;
        TextView recipeDescription;
        ImageView recipePicture;

        ViewHolder(final View itemView){
            super(itemView);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    context = itemView.getContext();
                    Intent intent = new Intent(context, pullRecipe.class);
                    Bundle b = new Bundle();
                    try {
                        b.putInt("recipeID", (Integer) currentItem.get("id"));
                        System.out.println(currentItem.get("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtras(b);
                    context.startActivity(intent);

                }
            });
            cv = (CardView) itemView.findViewById(R.id.cv);
            recipeTitle = (TextView) itemView.findViewById(R.id.recipeTitle);
            recipeDescription = (TextView) itemView.findViewById(R.id.recipeDescription);
            recipePicture = (ImageView) itemView.findViewById(R.id.recipePicture);

            FloatingActionButton fab = (FloatingActionButton) itemView.findViewById(R.id.recipeCardFAB);
            fab.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    try{
                        editor = itemView.getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                        context = view.getContext();

                        getRecipeInformation(new VolleyCallbackJSONObject() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                pulledRecipe = result;
                                setPreferences(editor, (String) recipeTitle.getText());

                            }
                        }, (int) currentItem.get("id"));

                        Toast.makeText(context, "Added to Personal Cookbook!", Toast.LENGTH_SHORT).show();

                    }catch(JSONException e){
                        e.printStackTrace();
                    }


                }
            });
        }
    }

    public void setPreferences(SharedPreferences.Editor editor, String title){
        editor.putString(title, pulledRecipe.toString());
        editor.apply();
    }

    private void getRecipeInformation(final VolleyCallbackJSONObject callback, int id){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
    // Provide a suitable constructor (depends on the kind of dataset)
    public RecipeListAdapter(ArrayList<JSONObject> myDataset) {
        values = myDataset;
    }

    @Override
    public int getItemCount(){
        return values.size();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_result, parent, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewholder, int i){
        try{
            System.out.println(values);
            String description = "Used Ingredients: " + values.get(i).get("usedIngredientCount").toString()
                    + "\nMissing Ingredients: " + values.get(i).get("missedIngredientCount").toString()
                    + "\nLikes: " + values.get(i).get("likes").toString();
            String url = (String)values.get(i).get("image");
            if(url.contains("null")){
              viewholder.recipePicture.setImageResource(R.drawable.recipe_pic_missing);
            }else Picasso.with(viewholder.itemView.getContext()).load(url).into(viewholder.recipePicture);
            viewholder.recipeTitle.setText((String)values.get(i).get("title"));
            viewholder.recipeDescription.setText(description);
            viewholder.currentItem = values.get(i);
        }catch(JSONException e){
            e.printStackTrace();
        }

    }


    public static ArrayList<JSONObject> getValues(){
        return values;
    }


}