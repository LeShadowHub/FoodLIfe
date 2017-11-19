package com.example.foodlife;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConversionsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> history = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversions);

        Button buttonGo = (Button) findViewById(R.id.buttonGo);



        mRecyclerView = (RecyclerView) findViewById(R.id.conversionHistory);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Adapter(history);
        mRecyclerView.setAdapter(mAdapter);


        buttonGo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                convertAmounts(new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        history.add(result);
                    }
                });
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                }, 5000);







            }
        });
    }

    private void convertAmounts(final VolleyCallback callback){


        String ingredientName = ((EditText)(findViewById(R.id.ingredientName))).getText().toString();
        double sourceAmount = Double.parseDouble(((EditText)(findViewById(R.id.amountSource))).getText().toString());
        String sourceUnit = ((EditText)(findViewById(R.id.sourceUnit))).getText().toString();
        String targetUnit = ((EditText)(findViewById(R.id.targetUnit))).getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

       String url =  "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/convert?ingredientName="+ ingredientName +"&sourceAmount="+sourceAmount+"&sourceUnit="+sourceUnit+"&targetUnit=" + targetUnit;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    callback.onSuccess((String)response.get("answer"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
}
