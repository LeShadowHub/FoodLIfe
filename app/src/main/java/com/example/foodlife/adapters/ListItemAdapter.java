package com.example.foodlife.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import com.example.foodlife.PantryListActivity;
import com.example.foodlife.R;
import com.example.foodlife.model.Item;

/**
 * Created by OEM on 12/2/2017.
 */

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    ItemClickListener itemClickListener;
    TextView item_title, item_description;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        item_title = (TextView) itemView.findViewById(R.id.item_title);
        item_description = (TextView) itemView.findViewById(R.id.item_description);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onCLick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), "DELETE");
    }
}

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder>{

    PantryListActivity pantryListActivity;
    List<Item> itemList;

    public ListItemAdapter(PantryListActivity pantryListActivity, List<Item> itemList) {
        this.pantryListActivity = pantryListActivity;
        this.itemList = itemList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(pantryListActivity.getBaseContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {

        // Set data for item
        holder.item_title.setText(itemList.get(position).getTitle());
        holder.item_description.setText(itemList.get(position).getDescription());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onCLick(View view, int position, boolean isLongClick) {
                // When user select item, data will auto set for Edit Text View
                pantryListActivity.title.setText(itemList.get(position).getTitle());
                pantryListActivity.description.setText(itemList.get(position).getDescription());

                pantryListActivity.isUpdate = true;
                pantryListActivity.idUpdate = itemList.get(position).getId();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
