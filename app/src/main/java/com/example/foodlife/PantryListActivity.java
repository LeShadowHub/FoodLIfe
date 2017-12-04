package com.example.foodlife;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.foodlife.adapters.ListItemAdapter;
import com.example.foodlife.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class PantryListActivity extends AppCompatActivity {

    List<Item> itemList = new ArrayList<>();
    FirebaseFirestore db;
    CollectionReference colRef;
    CollectionReference ingredientRef;

    private String user_id;
    private String category;

    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    public MaterialEditText title, expiration;
    public boolean isUpdate = false;
    public String idUpdate = "";

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    ListItemAdapter adapter;

    AlertDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toast.makeText(PantryListActivity.this, "Tap to Edit. Press and Hold to Delete", Toast.LENGTH_SHORT).show();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            user_id = extras.getString("USER_ID");
            category = extras.getString("CATEGORY");
        }

        // Init FireStore
        db = FirebaseFirestore.getInstance();
        colRef = db.collection("Users").document(user_id).collection(category + "List");
        ingredientRef = db.collection("Users").document(user_id).collection("ingredientList");


        // View
        dialog = new SpotsDialog(this);
        title = (MaterialEditText) findViewById(R.id.titlePantry);
        expiration = (MaterialEditText) findViewById(R.id.descriptionPantry);
        fab = (FloatingActionButton) findViewById(R.id.fabPantry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add New
                if(!isUpdate){
                    //Toast.makeText(PantryListActivity.this, "Clicked Set", Toast.LENGTH_SHORT).show();
                    try {
                        Date date = sdf.parse(expiration.getText().toString());
                        setData(title.getText().toString(), date);
                    } catch (ParseException e) {
                        Toast.makeText(PantryListActivity.this, "Error with Adding item", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else{
                    //Toast.makeText(PantryListActivity.this, "Clicked Update", Toast.LENGTH_SHORT).show();
                    try {
                        Date date = sdf.parse(expiration.getText().toString());
                        updateData(title.getText().toString(), date);
                        isUpdate = false;
                    } catch (ParseException e) {
                        isUpdate = false;
                        Toast.makeText(PantryListActivity.this, "Error with Updating item", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

            }
        });

        // Picking a date
        expiration.setShowSoftInputOnFocus(false);

        expiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        PantryListActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String formattedDate = sdf.format(datePicker.getCalendarView().getDate());
                try {
                    Date date = sdf.parse(formattedDate);
                    String newDate = sdf.format(date);
                    expiration.setText(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };

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

        ingredientRef.document(itemList.get(index).getId()).delete();
    }

    private void updateData(String title, Date expiration) {
        colRef.document(idUpdate)
                .update("title",title,"expiration",expiration)
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

        ingredientRef.document(idUpdate).update("title",title,"expiration",expiration);
    }

    private void setData(String title, Date expiration) {
        // Random id
        String id = UUID.randomUUID().toString();
        Map<String, Object> item = new HashMap<>();
        item.put("id", id);
        item.put("title", title);
        item.put("expiration", expiration);

        colRef.document(id)
                .set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Refresh data
                Toast.makeText(PantryListActivity.this, "Item Added!", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });

        ingredientRef.document(id).set(item);
    }

    private void loadData() {
        dialog.show();
        if(itemList.size() > 0)
            itemList.clear();
        colRef.orderBy("expiration")
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
                    adapter = new ListItemAdapter(PantryListActivity.this, itemList);
                    listItem.setAdapter(adapter);
                    dialog.dismiss();

                    title.getText().clear();
                    expiration.getText().clear();
                    expiration.clearFocus();
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
