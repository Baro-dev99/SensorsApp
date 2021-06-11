package com.example.sensorsapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class BarCustomView extends View {
    private Paint staticBarPaint, dynBarPaint, blackPaint;
    private float sensorFirstValue = 0, width = 0, height = 0;
    public static float barLength;

    public BarCustomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray extraAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SensorMonitorCustView, 0, 0);

        staticBarPaint = new Paint();
        int staticBarColor = extraAttributes.getInteger(R.styleable.SensorMonitorCustView_static_bar_color, 0);
        staticBarPaint.setColor(staticBarColor);

        dynBarPaint = new Paint();
        int dynBarColor = extraAttributes.getInteger(R.styleable.SensorMonitorCustView_dynamic_bar_color, 0);
        dynBarPaint.setColor(dynBarColor);

        blackPaint = new Paint();
        blackPaint.setColor(Color.DKGRAY);
        blackPaint.setTextSize(40);
    }

    public float sensorFirstValue() {
        return sensorFirstValue;
    }

    public void setSensorFirstValue(float sensorFirstValue) {
        this.sensorFirstValue = sensorFirstValue;
        invalidate();
        requestLayout();
    }

    public Paint getDynBarPaint() {
        return dynBarPaint;
    }

    public void setDynBarPaint(Paint dynBarPaint) {
        this.dynBarPaint = dynBarPaint;
        invalidate();
        requestLayout();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (width == 0)
            width = getMeasuredWidth();
        if (height == 0)
            height = getMeasuredHeight();

        barLength = (3 * height) / 4;
        float barWidth = width / 4 + 100;

        canvas.drawRect(width / 4, 100, barWidth + 100, barLength, staticBarPaint);
        canvas.drawRect(width / 4, barLength / 2 + 50 - sensorFirstValue, barWidth + 100, barLength, dynBarPaint);
        canvas.drawLine(width / 4, barLength / 2 + 50, (3 * width) / 4 - 55, barLength / 2 + 50, blackPaint);
        canvas.drawText("Max", width / 4 + 65, 80, blackPaint);
        canvas.drawText("Min", width / 4 + 65, barLength + 50, blackPaint);
        canvas.drawText("0", width / 4 - 60, barLength / 2 + 60, blackPaint);
    }
}
