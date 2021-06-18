package com.example.sensorsapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;

public class WorkerThread extends Thread implements SensorEventListener {
    private Handler handler;
    private Sensor sensor;
    private StringBuilder sensorValuesStr = new StringBuilder();;
    private Message message;
    private Bundle bundle = new Bundle();;
    int counter = 0;

    public WorkerThread(Handler handler, Sensor sensor) {
        this.handler = handler;
        this.sensor = sensor;
    }

    @Override
    public void run() {
        super.run();
        MainActivity.sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorValuesStr.setLength(0);
        message = new Message();
        bundle.clear();
        counter++;

        for (int i = 0; i < event.values.length; i++)
            sensorValuesStr.append(System.lineSeparator()).append(event.values[i]);
        sensorValuesStr.append(System.lineSeparator()).append("Method call: " + counter);
        bundle.putString("sensorValues", sensorValuesStr.toString());

        int size = event.values.length;
        if(size > 4) size = 4;

        bundle.putString("diagramSize", String.valueOf(size));
        bundle.putString("maxRange", String.valueOf(event.sensor.getMaximumRange()));

        for(int i = 0; i < size; i++)
            bundle.putString("diagramBar" + i, String.valueOf(event.values[i]));

        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
