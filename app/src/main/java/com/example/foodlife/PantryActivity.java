package com.example.foodlife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.AdapterView;

import com.example.foodlife.R;
import com.example.foodlife.adapters.FoodGroupAdapter;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class PantryActivity extends AppCompatActivity {

    private String user_id;

    private FeatureCoverFlow coverFlow;
    private FoodGroupAdapter foodGroupAdapter;
    private List<FoodGroup> foodGroupList = new ArrayList<>();
    private TextSwitcher mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            user_id = extras.getString("USER_ID");
        }

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

        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent0 = new Intent(PantryActivity.this, PantryListActivity.class);
                        intent0.putExtra("USER_ID", user_id);
                        intent0.putExtra("CATEGORY", "grain");
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(PantryActivity.this, PantryListActivity.class);
                        intent1.putExtra("USER_ID", user_id);
                        intent1.putExtra("CATEGORY", "meat");
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(PantryActivity.this, PantryListActivity.class);
                        intent2.putExtra("USER_ID", user_id);
                        intent2.putExtra("CATEGORY", "fruit");
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(PantryActivity.this, PantryListActivity.class);
                        intent3.putExtra("USER_ID", user_id);
                        intent3.putExtra("CATEGORY", "veggie");
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(PantryActivity.this, PantryListActivity.class);
                        intent4.putExtra("USER_ID", user_id);
                        intent4.putExtra("CATEGORY", "dairy");
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(PantryActivity.this, PantryListActivity.class);
                        intent5.putExtra("USER_ID", user_id);
                        intent5.putExtra("CATEGORY", "snack");
                        startActivity(intent5);
                        break;
                }
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
