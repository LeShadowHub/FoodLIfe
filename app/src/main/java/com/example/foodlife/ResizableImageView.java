package com.example.foodlife;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by OEM on 11/10/2017.
 */

public class ResizableImageView extends android.support.v7.widget.AppCompatImageView{


    public ResizableImageView(Context context) {
        super(context);
    }

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec) ;
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width / 2 - 18, height);
        } else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
