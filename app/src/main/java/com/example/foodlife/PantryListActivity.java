package com.example.foodlife;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.foodlife.adapters.ListItemAdapter;
import com.example.foodlife.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class PantryListActivity extends AppCompatActivity {

    List<Item> itemList = new ArrayList<>();
    FirebaseFirestore db;
    CollectionReference colRef;

    private String user_id;
    private String category;

    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    public MaterialEditText title, description;
    public boolean isUpdate = false;
    public String idUpdate = "";

    ListItemAdapter adapter;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            user_id = extras.getString("USER_ID");
            category = extras.getString("CATEGORY");
        }

        // Init FireStore
        db = FirebaseFirestore.getInstance();
        colRef = db.collection("Users").document(user_id).collection(category + "List");

        // View
        dialog = new SpotsDialog(this);
        title = (MaterialEditText) findViewById(R.id.titlePantry);
        description = (MaterialEditText) findViewById(R.id.descriptionPantry);
        fab = (FloatingActionButton) findViewById(R.id.fabPantry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add New
                if(!isUpdate){
                    //Toast.makeText(PantryListActivity.this, "Clicked Set", Toast.LENGTH_SHORT).show();
                    setData(title.getText().toString(), description.getText().toString());

                } else{
                    //Toast.makeText(PantryListActivity.this, "Clicked Update", Toast.LENGTH_SHORT).show();
                    updateData(title.getText().toString(), description.getText().toString());
                    isUpdate = false;
                }

            }
        });

        listItem = (RecyclerView) findViewById(R.id.listTodo);
        listItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);

        loadData(); // Load data from FireStore

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle().equals("DELETE")){
            deleteItem(item.getOrder());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteItem(int index) {
        colRef.document(itemList.get(index).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PantryListActivity.this, "Item Deleted!", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                });
    }

    private void updateData(String title, String description) {
        colRef.document(idUpdate)
                .update("title",title,"description",description)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PantryListActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Realtime update refresh data
        colRef.document(idUpdate)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        loadData();
                    }
                });
    }

    private void setData(String title, String description) {
        // Random id
        String id = UUID.randomUUID().toString();
        Map<String, Object> item = new HashMap<>();
        item.put("id", id);
        item.put("title", title);
        item.put("description", description);

        colRef.document(id)
                .set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Refresh data
                Toast.makeText(PantryListActivity.this, "Item Added!", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });
    }

    private void loadData() {
        dialog.show();
        if(itemList.size() > 0)
            itemList.clear();
        colRef
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot doc:task.getResult()){
                        Item item = new Item(doc.getString("id"),
                                            doc.getString("title"),
                                            doc.getString("description"));

                        itemList.add(item);
                    }
                    adapter = new ListItemAdapter(PantryListActivity.this, itemList);
                    listItem.setAdapter(adapter);
                    dialog.dismiss();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PantryListActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}
