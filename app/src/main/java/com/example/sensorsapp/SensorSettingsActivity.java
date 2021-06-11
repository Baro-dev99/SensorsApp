package com.example.sensorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SensorSettingsActivity extends AppCompatActivity {
    private List<MySensor> mySensorList = new ArrayList<>();
    private SensorAdapter sensorAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MyLog", "Sensor Settings : onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_settings);

        // get Main Activity sp
        sharedPreferences = getSharedPreferences(MainActivity.SP_NAME, MODE_PRIVATE);

        // get Main Activity device sensors
        List<Sensor> deviceSensors = MainActivity.deviceSensors;

        // filling the list of mySensors
        for (int i = 0; i < deviceSensors.size(); i++)
            mySensorList.add(i, new MySensor(deviceSensors.get(i), false));

        sensorAdapter = new SensorAdapter(this, mySensorList);
        ListView listView = findViewById(R.id.sensorList);
        listView.setAdapter(sensorAdapter);

        if (sharedPreferences.getString("selected_sensors_backup", null) != null) {
            // Get selected sensors from shared preference
            String[] selectedSensors = sharedPreferences.getString("selected_sensors_backup", null).split(",");

            // check selected sensors checkboxes
            for (int i = 0; i < selectedSensors.length; i++)
                mySensorList.get(Integer.parseInt(selectedSensors[i])).setSelected(true);
            sensorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("MyLog", "Sensor Settings : onDestroy()");
        super.onDestroy();
//        mySensorList.clear();
//        sensorAdapter.notifyDataSetChanged();
    }

    public void savePreferences(View view) {
        // saving selected sensors indexes in a shared preference string
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < mySensorList.size(); i++)
            if (mySensorList.get(i).isSelected() == true)
                str.append(i).append(",");

        if (str.length() != 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("selected_sensors", str.toString());
            editor.apply();
            this.finish();
        }
        else {
            Toast.makeText(this, "Select at least 1 sensor to monitor!", Toast.LENGTH_LONG).show();
        }
    }

    public void checkboxClick(View view) {
        int position = (Integer) view.getTag();
        if (mySensorList.get(position).isSelected() == false)
            mySensorList.get(position).setSelected(true);
        else
            mySensorList.get(position).setSelected(false);
        sensorAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear)
            for (int i = 0; i < mySensorList.size(); i++)
                mySensorList.get(i).setSelected(false);

        if (item.getItemId() == R.id.select_all)
            for (int i = 0; i < mySensorList.size(); i++)
                mySensorList.get(i).setSelected(true);

        sensorAdapter.notifyDataSetChanged();
        return true;
    }
}