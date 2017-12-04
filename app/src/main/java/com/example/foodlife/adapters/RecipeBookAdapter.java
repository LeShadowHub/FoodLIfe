package com.example.foodlife.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodlife.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.foodlife.displayRecipeBook;

public class RecipeBookAdapter extends RecyclerView.Adapter<RecipeBookAdapter.ViewHolder> {

    public static final String MY_PREFS_NAME = "SavedRecipes";

    private static ArrayList<JSONObject> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public JSONObject currentItem;
        private int currentIndex;

        CardView cv;
        TextView recipeTitle;
        TextView recipeDescription;
        ImageView recipePicture;

        ViewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, displayRecipeBook.class);
                    try {

                        intent.putExtra("title", (String) currentItem.get("title"));
    //need image

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(intent);

                }
            });
            cv = (CardView) itemView.findViewById(R.id.cv);
            recipeTitle = (TextView) itemView.findViewById(R.id.recipeTitle);
            recipeDescription = (TextView) itemView.findViewById(R.id.recipeDescription);
            recipePicture = (ImageView) itemView.findViewById(R.id.recipePicture);

            FloatingActionButton fab = (FloatingActionButton) itemView.findViewById(R.id.recipeCardFAB);
            fab.setImageResource(R.drawable.ic_delete_white_36dp);
            fab.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                    SharedPreferences.Editor editor = itemView.getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                    editor.remove((String) recipeTitle.getText());  //delete from preference list
                    editor.apply();
                    values.remove(currentIndex);
                    notifyDataSetChanged();
                }
            });
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public RecipeBookAdapter(ArrayList<JSONObject> myDataset) {
        values = myDataset;
    }

    @Override
    public int getItemCount(){
        return values.size();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecipeBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_result, parent, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewholder, int i){
        try{

            String url = (String)values.get(i).get("image");
            if(url.contains("null")){
               viewholder.recipePicture.setImageResource(R.drawable.recipe_pic_missing);
            }else Picasso.with(viewholder.itemView.getContext()).load(url).into(viewholder.recipePicture);
            viewholder.recipeTitle.setText((String)values.get(i).get("title"));
            viewholder.currentItem = values.get(i);
            viewholder.currentIndex = i;

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<JSONObject> getValues(){
        return values;
    }


}