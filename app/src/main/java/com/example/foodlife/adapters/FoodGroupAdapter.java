package com.example.foodlife.adapters;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodlife.FoodGroup;
import com.example.foodlife.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by OEM on 11/20/2017.
 */

public class FoodGroupAdapter extends BaseAdapter{
    private List<FoodGroup> foodGroupList;
    private Context mContext;

    public FoodGroupAdapter(List<FoodGroup> foodGroupList, Context mContext){
        this.foodGroupList = foodGroupList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return foodGroupList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodGroupList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroupt) {
        View rowView = view;
        if(rowView == null){
            rowView = LayoutInflater.from(mContext).inflate(R.layout.pantry_item,null);
            TextView label = (TextView) rowView.findViewById(R.id.pantry_label);
            ImageView image = (ImageView) rowView.findViewById(R.id.pantry_image);

            // Set Data
            Picasso.with(mContext).load(foodGroupList.get(position).getImage())
                    .into(image);
            label.setText(foodGroupList.get(position).getName());
        }
        return rowView;
    }
}
