package com.example.sensorsapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class SensorAdapter extends ArrayAdapter<MySensor> {
    static List<MySensor> list_sensor;

    public SensorAdapter(Context context, List<MySensor> mySensorList) {
        super(context, android.R.layout.simple_list_item_1, mySensorList);
        list_sensor = mySensorList;
    }

    @Override
    public View getView (int position, View row, ViewGroup parent) {
//        View row = convertView;
        MySensor currentSensor = list_sensor.get(position);
        CheckBox checkBox;

        // optimised
        if (row == null) {
            LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.row, parent, false);
        }

        // NOT optimised: view is redrawn every time
//        LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
//        row = inflater.inflate(R.layout.row, parent, false);

        // Adjust row Items
        ((TextView)row.findViewById(R.id.row_name)).setText(currentSensor.getSensor().getName());
        checkBox = (CheckBox)row.findViewById(R.id.row_checkbox);
        checkBox.setTag(position);

        if (currentSensor.isSelected() == true)
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);

        return row;
    }

    @Override
    public int getCount() {
        return list_sensor.size();
    }
}
