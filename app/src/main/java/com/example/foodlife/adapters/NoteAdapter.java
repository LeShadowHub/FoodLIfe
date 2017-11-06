package com.example.foodlife.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.foodlife.Note;
import com.example.foodlife.R;

import java.util.ArrayList;

/**
 * Created by OEM on 11/5/2017.
 */

public class NoteAdapter extends ArrayAdapter<Note> {

    public static final int WRAP_CONTENT_LENGTH = 50;

    public NoteAdapter(Context context, int resource, ArrayList<Note> notes){
        super(context, resource, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_note, null);
        }

        Note note = getItem(position);

        if(note != null){
            TextView title = (TextView) convertView.findViewById(R.id.list_note_title);
            TextView date = (TextView) convertView.findViewById(R.id.list_note_date);
            TextView content = (TextView) convertView.findViewById(R.id.list_note_content);

            title.setText(note.getTitle());
            date.setText(note.getDateTimeFormatted(getContext()));

            // Preview content no more than 50 chars or one line
            int toWrap = WRAP_CONTENT_LENGTH;
            int lineBreakIndex = note.getContent().indexOf('\n');
            if(note.getContent().length() > WRAP_CONTENT_LENGTH || lineBreakIndex < WRAP_CONTENT_LENGTH){
                if(lineBreakIndex < WRAP_CONTENT_LENGTH){
                    toWrap = lineBreakIndex;
                }
                if(toWrap > 0){
                    content.setText(note.getContent().substring(0,toWrap) + "...");
                } else{
                    content.setText(note.getContent());
                }

            } else{
                content.setText(note.getContent());
            }
        }
        return convertView;
    }
}
