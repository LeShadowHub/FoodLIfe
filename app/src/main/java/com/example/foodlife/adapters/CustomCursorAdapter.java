package com.example.foodlife.adapters;

/**
 * Created by OEM on 12/1/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodlife.R;
import com.example.foodlife.data.TaskContract;

/**
 * Custom Adapter creates and binds ViewHolder, that hold descrip and priority of an item
 * to the RecyclerView to efficiently display data
 */
public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder>{

    private Cursor mCursor;
    private Context mContext;

    public CustomCursorAdapter(Context mContext){
        this.mContext = mContext;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView
     *
     * @return new TaskViewHolder that holds view for each item
     */
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate grocery_task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.grocery_task_layout, parent, false);

        return new TaskViewHolder(view);
    }

    /**
     * Called by RecyclerView to display data at specified position in Cursor
     *
     * @param holder ViewHolder to bind Cursor data to
     * @param position position of data in Cursor
     */
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        // Indices for _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(TaskContract.ItemEntry._ID);
        int descriptionIndex = mCursor.getColumnIndex(TaskContract.ItemEntry.COLUMN_DESCRIPTION);
        int priorityIndex = mCursor.getColumnIndex(TaskContract.ItemEntry.COLUMN_PRIORITY);

        mCursor.moveToPosition(position);

        // Determine values of wanted data
        final int id = mCursor.getInt(idIndex);
        String description = mCursor.getString(descriptionIndex);
        int priority = mCursor.getInt(priorityIndex);

        // Set values
        holder.itemView.setTag(id);
        holder.itemDescriptionView.setText(description);

        // Programmatically set text and color for priority TextView
        String priorityString = "" + priority;  // converts int to String
        holder.priorityView.setText(priorityString);

        GradientDrawable priorityCircle = (GradientDrawable) holder.priorityView.getBackground();
        // Get appropriate background color based on priority
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);

    }

    /**
     * Helper method for selecting priority circle color
     * p1 = red, p2 = orange, p3 = yellow
     */
    private int getPriorityColor(int priority){
        int priorityColor = 0;

        switch (priority){
            case 1: priorityColor = ContextCompat.getColor(mContext, R.color.materialRed);
                break;
            case 2: priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange);
                break;
            case 3: priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow);
                break;
            default: break;
        }
        return priorityColor;
    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * When data changes and requery occurs, this function swaps old cursor
     * with newly updated Cursor that is passed in
     */
    public Cursor swapCursor(Cursor c){
        // Check if this cursor is same as previous
        if(mCursor == c){
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        // Check if valid then update
        if(c != null){
            this.notifyDataSetChanged();
        }
        return temp;
    }

    // Inner class for creating ViewHolders
    class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView itemDescriptionView;
        TextView priorityView;

        public TaskViewHolder(View itemView){
            super(itemView);

            itemDescriptionView = (TextView) itemView.findViewById(R.id.itemDescription);
            priorityView = (TextView) itemView.findViewById(R.id.priorityTextView);
        }
    }
}
