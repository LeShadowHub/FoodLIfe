package com.example.foodlife;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodlife.adapters.NotificationAdapter;
import com.example.foodlife.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String user_id;
    
    // FireStore
    ArrayList<String> ingredients = new ArrayList<>();
    List<Item> itemList = new ArrayList<>();
    FirebaseFirestore db;
    CollectionReference ingredientRef;
    
    RecyclerView listNotification;
    RecyclerView.LayoutManager layoutManager;
    
    NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNote = (Button) findViewById(R.id.btnNote);
        ResizableImageView pantryView = (ResizableImageView) findViewById(R.id.btnPantry);
        ResizableImageView utilityView = (ResizableImageView) findViewById(R.id.btnUtility);
        ResizableImageView groceryView = (ResizableImageView) findViewById(R.id.btnGrocery);
        ResizableImageView recipeView = (ResizableImageView) findViewById(R.id.btnRecipe);
        
        listNotification = (RecyclerView) findViewById(R.id.notificationList); 
        listNotification.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listNotification.setLayoutManager(layoutManager);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
             user_id = extras.getString("USER_ID");
        }

        // Init FireStore
        db = FirebaseFirestore.getInstance();
        ingredientRef = db.collection("Users").document(user_id).collection("ingredientList");

        //loadData();

        pantryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPantryActivity = new Intent(MainActivity.this, PantryActivity.class);
                newPantryActivity.putExtra("USER_ID", user_id);
                startActivity(newPantryActivity);
            }
        });

        utilityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newUtilityActivity = new Intent(MainActivity.this, UtilityActivity.class);
                startActivity(newUtilityActivity);
            }
        });

        groceryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGroceryActivity = new Intent(MainActivity.this, GroceryActivity.class);
                startActivity(newGroceryActivity);
            }
        });

        recipeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ingredientRef
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(DocumentSnapshot doc:task.getResult()){
                                    String ingredient = doc.getString("title");
                                    ingredients.add(ingredient);
                                }

                                Intent newRecipeActivity = new Intent(MainActivity.this, RecipeActivity.class);
                                newRecipeActivity.putStringArrayListExtra("PANTRY",ingredients);
                                startActivity(newRecipeActivity);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newNoteListActivity = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(newNoteListActivity);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }

    private void loadData() {
        if(itemList.size() > 0)
            itemList.clear();
        ingredientRef.orderBy("expiration")
                .limit(15)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc:task.getResult()){
                            Item item = new Item(doc.getString("id"),
                                    doc.getString("title"),
                                    doc.getDate("expiration"));

                            itemList.add(item);
                        }
                        adapter = new NotificationAdapter(MainActivity.this, itemList);
                        listNotification.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
