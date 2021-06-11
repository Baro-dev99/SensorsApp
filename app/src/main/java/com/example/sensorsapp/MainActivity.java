package com.example.sensorsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    public static String SP_NAME = "my_preferences";
    public static SensorManager sensorManager;
    public static List<Sensor> deviceSensors;

    private List<Sensor> selectedSensorsList = new ArrayList<>();
    private SensorViewPagerAdapter sensorViewPagerAdapter;
    private SharedPreferences sharedPreferences;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MyLog", "Main Activity : onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // get all-type device sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // if sp does not exit -> it is then created
        sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);

        if (sharedPreferences.getString("selected_sensors", null) == null) {
            // no selected sensors -> select some in sensor settings activity
            startActivity(new Intent(this, SensorSettingsActivity.class));
        }
    }

    @Override
    protected void onResume() {
        Log.d("MyLog", "Main Activity : onResume()");
        super.onResume();

        if (sharedPreferences.getString("selected_sensors", null) != null ) {
            // Get selected sensors from shared preference
            String[] selectedSensors = sharedPreferences.getString("selected_sensors", null).split(",");

            // get selected sensors
            for (int i = 0; i < selectedSensors.length; i++)
                selectedSensorsList.add(i, deviceSensors.get(Integer.parseInt(selectedSensors[i])));

//            ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
            if(sensorViewPagerAdapter == null) {
                sensorViewPagerAdapter = new SensorViewPagerAdapter(getSupportFragmentManager(), 0, selectedSensorsList);
                viewPager.setAdapter(sensorViewPagerAdapter);
            }
            sensorViewPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        Log.d("MyLog", "Main Activity : onStop()");
        super.onStop();
        selectedSensorsList.clear();
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        Log.d("MyLog", "Main Activity : onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.preferences) {
            String backup = sharedPreferences.getString("selected_sensors", null);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("selected_sensors_backup", backup);
            editor.putString("selected_sensors", null);
            editor.apply();

            this.finish();
            startActivity(getIntent());
        }
        return true;
    }

}