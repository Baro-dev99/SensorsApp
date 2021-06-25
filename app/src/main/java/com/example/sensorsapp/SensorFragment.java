package com.example.sensorsapp;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SensorFragment extends Fragment implements SensorEventListener {
    private Sensor sensor;
    private TextView sensorValues;
    private DiagramView diagramBars[] = new DiagramView[4];

    private Handler handler;
    private WorkerThread2 thread;

    private int counter = 0;

    public SensorFragment() {
        // Required empty public constructor
    }

    public static SensorFragment newInstance(Sensor sensor) {
        SensorFragment fragment = new SensorFragment();
        fragment.sensor= sensor;
        return fragment;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MainActivity.sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String str = msg.getData().getString("sensorValues");
                sensorValues.setText("Sensor Values:" + str);

                int size = Integer.parseInt(msg.getData().getString("diagramSize"));
                int workerID = Integer.parseInt(msg.getData().getString("workerID"));
                String workerName = msg.getData().getString("workerName");
                float maxRange = Float.parseFloat(msg.getData().getString("maxRange"));

                for (int i = 0; i < size; i++) {
                    float value = Float.parseFloat(msg.getData().getString("diagramBar" + i));
                    diagramBars[i].setSensorValue((value * DiagramView.BAR_LENGTH / 2) / maxRange);
                }

                Log.d("MyLog", "SensorFragment (handler) - ["
                        + Thread.currentThread().getName() + ":" + Thread.currentThread().getId()
                        + "] -> Received data from [" + workerName + ":" + workerID + "] -> Views Updated");
            }
        };

//        thread = new WorkerThread(handler, sensor);
//        thread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MyLog", "SensorFragment : " + sensor.getName() + ": onResume()");
        MainActivity.sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
//        thread = new WorkerThread(handler, sensor);
//        thread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View frameLayout =  inflater.inflate(R.layout.fragment_sensor, container, false);

        TextView name = frameLayout.findViewById(R.id.sensor_name_frag);
        TextView vendor = frameLayout.findViewById(R.id.sensor_vendor_frag);
        TextView version = frameLayout.findViewById(R.id.sensor_version_frag);
        TextView type = frameLayout.findViewById(R.id.sensor_type_frag);
        TextView power = frameLayout.findViewById(R.id.sensor_power_frag);
        TextView maxRange = frameLayout.findViewById(R.id.sensor_maxrange_frag);

        diagramBars[0] = frameLayout.findViewById(R.id.diagram_bar_1);
        diagramBars[1] = frameLayout.findViewById(R.id.diagram_bar_2);
        diagramBars[2] = frameLayout.findViewById(R.id.diagram_bar_3);
        diagramBars[3] = frameLayout.findViewById(R.id.diagram_bar_4);

        sensorValues = frameLayout.findViewById(R.id.sensor_values);

        name.setText("Name: " + sensor.getName());
        vendor.setText("Vendor: " + sensor.getVendor());
        version.setText("Version: " + sensor.getVersion());
        power.setText("Power: " + sensor.getPower());
        maxRange.setText("Max. Range: " + sensor.getMaximumRange());

        // just to get the string type of sensor
        StringBuilder typeStr = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            typeStr.append(sensor.getStringType().substring(15));
            type.setText("Type: " + typeStr.toString());
        }
        else {
            type.setText("Type: " + sensor.getType());
        }

        return frameLayout;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("MyLog", "SensorFragment : " + sensor.getName() + ": onStop()");
        MainActivity.sensorManager.unregisterListener(this);
//        MainActivity.sensorManager.unregisterListener(thread);
//        thread.interrupt();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onSensorChanged(SensorEvent event) {
        // to prevent the UI thread from creating a huge number of Worker Threads.
        if(counter % 50 == 0) {
            WorkerThread2 thread = new WorkerThread2(handler, event.values, event.sensor.getMaximumRange());
            thread.start();
        }
        counter ++;


//        StringBuilder sensorValuesStr = new StringBuilder();
//
//        for (int i = 0; i < values.length; i++)
//            sensorValuesStr.append(System.lineSeparator()).append(values[i]);
//        sensorValues.setText("Sensor Values:" + sensorValuesStr.toString());
//
//        int size = values.length;
//        if(size > 4) size = 4;
//        for(int i = 0; i < size; i++)
//            diagramBars[i].setSensorValue((values[i] * DiagramView.BAR_LENGTH / 2) / event.sensor.getMaximumRange());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}