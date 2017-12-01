package com.example.foodlife;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    private static ArrayList<JSONObject> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public JSONObject currentItem;

        CardView cv;
        TextView recipeTitle;
        TextView recipeDescription;
        ImageView recipePicture;

        ViewHolder(final View itemView){
            super(itemView);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, pullRecipe.class);
                    Bundle b = new Bundle();
                    try {
                        b.putInt("recipeID", (Integer) currentItem.get("id"));
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
        }
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