package com.example.sensorsapp;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class WorkerThread extends Thread{
    private Handler handler;
    private float[] sensorValues;
    private float maximumRange;

    public WorkerThread(Handler handler, float[] sensorValues, float maximumRange) {
        this.handler = handler;
        this.sensorValues = sensorValues;
        this.maximumRange = maximumRange;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        super.run();

        StringBuilder sensorValuesStr = new StringBuilder();
        Message message = new Message();;
        Bundle bundle = new Bundle();

        for (float sensorValue : sensorValues)
            sensorValuesStr.append(System.lineSeparator()).append(sensorValue);
        bundle.putString("sensorValues", sensorValuesStr.toString());

        int size = sensorValues.length;
        if(size > 4) size = 4;

        bundle.putString("diagramSize", String.valueOf(size));
        bundle.putString("maxRange", String.valueOf(maximumRange));

        for(int i = 0; i < size; i++)
            bundle.putString("diagramBar" + i, String.valueOf(sensorValues[i]));

        bundle.putString("workerID", String.valueOf(Thread.currentThread().getId()));
        bundle.putString("workerName", Thread.currentThread().getName());

        message.setData(bundle);
        handler.sendMessage(message);

        Log.d("MyLog", "WorkerThread2 (run) - ["
                + Thread.currentThread().getName() + ":" + Thread.currentThread().getId()
                + "] -> Job done");
    }

}
