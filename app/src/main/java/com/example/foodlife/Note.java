package com.example.foodlife;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by OEM on 10/27/2017.
 */

public class Note implements Serializable{

    private long mDateTime;
    private String mTitle;
    private String mContent;

    public Note(long dateTime, String title, String content){
        mContent = content;
        mDateTime = dateTime;
        mTitle = title;
    }

    public void setDateTime(long dateTime) {
        mDateTime = dateTime;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public long getDateTime() {
        return mDateTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public String getDateTimeFormatted(Context context){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",
                context.getResources().getConfiguration().getLocales().get(0));
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(mDateTime));
    }


}
