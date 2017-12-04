package com.example.foodlife.adapters;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodlife.MainActivity;
import com.example.foodlife.R;
import com.example.foodlife.model.Item;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by OEM on 12/2/2017.
 */


class NotificationViewHolder extends RecyclerView.ViewHolder{

    ItemClickListener itemClickListener;
    TextView notification_title, notification_description;

    public NotificationViewHolder(View itemView) {
        super(itemView);

        notification_title = (TextView) itemView.findViewById(R.id.notification_title);
        notification_description = (TextView) itemView.findViewById(R.id.notification_description);
    }
}

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder>{

    MainActivity mainActivity;
    List<Item> itemList;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public NotificationAdapter(MainActivity mainActivity, List<Item> itemList) {
        this.mainActivity = mainActivity;
        this.itemList = itemList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainActivity.getBaseContext());
        View view = inflater.inflate(R.layout.list_notification,parent,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {

        // Set data for item
        holder.notification_title.setText(itemList.get(position).getTitle());

        String newDate = sdf.format(itemList.get(position).getExpiration());
        holder.notification_description.setText(newDate);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
