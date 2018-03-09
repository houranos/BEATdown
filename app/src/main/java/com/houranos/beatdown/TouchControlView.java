package com.houranos.beatdown;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.view.View;

public class TouchControlView extends View {
    LayoutInflater inflater;
    public TouchControlView(Context context){
        super(context);
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_touch_control, null);
    }

    public TouchControlView(Context context, AttributeSet attrs){
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_touch_control, null);
    }

    public TouchControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_touch_control, null);
    }

    public TouchControlView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_touch_control, null);
    }


    public boolean onTouchEvent(MotionEvent event){
        if(event.getRawY() < 100){
            Log.d("Debug", "touch disabled");
        } else {
            Log.d("Debug", "touch enabled");
            super.onTouchEvent(event);
        }
        return true;
    }
}
