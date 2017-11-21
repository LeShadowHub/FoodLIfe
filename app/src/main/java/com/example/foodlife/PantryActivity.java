package com.example.foodlife;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.foodlife.R;
import com.example.foodlife.adapters.FoodGroupAdapter;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class PantryActivity extends AppCompatActivity {

    private FeatureCoverFlow coverFlow;
    private FoodGroupAdapter foodGroupAdapter;
    private List<FoodGroup> foodGroupList = new ArrayList<>();
    private TextSwitcher mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        initData();
        mTitle = (TextSwitcher) findViewById(R.id.pantryTitle);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(PantryActivity.this);
                TextView txt = (TextView) inflater.inflate(R.layout.pantry_title, null);
                return txt;
            }
        });

        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        foodGroupAdapter = new FoodGroupAdapter(foodGroupList, this);
        coverFlow = (FeatureCoverFlow) findViewById(R.id.coverFlow);
        coverFlow.setAdapter(foodGroupAdapter);

        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(foodGroupList.get(position).getName());
            }

            @Override
            public void onScrolling() {

            }
        });
    }

    private void initData() {

        foodGroupList.add(new FoodGroup("Grains", R.drawable.grain));
        foodGroupList.add(new FoodGroup("Meats", R.drawable.meat));
        foodGroupList.add(new FoodGroup("Fruits", R.drawable.fruit));
        foodGroupList.add(new FoodGroup("Vegetables", R.drawable.vegetables));
        foodGroupList.add(new FoodGroup("Dairy Products", R.drawable.dairy));
        foodGroupList.add(new FoodGroup("Snacks/Desserts", R.drawable.dessert));
    }
}
