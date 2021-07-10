package com.example.sensorsapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DiagramView extends View {
    public static final float BAR_LENGTH = 500;

    private final Paint paint;
    private float sensorValue = 0;

    public DiagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray extraAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DiagramView, 0, 0);

        paint = new Paint();
        int color = extraAttributes.getInteger(R.styleable.DiagramView_color,0);
        paint.setColor(color);
    }

//    public float getSensorValue() {
//        return sensorValue;
//    }

    public void setSensorValue(float sensorValue) {
        this.sensorValue = sensorValue;
        invalidate();
        requestLayout();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(10, BAR_LENGTH / 2 - sensorValue, 100, BAR_LENGTH, paint);
    }
}
