package com.example.sensorsapp;

import android.hardware.Sensor;

public class MySensor {
    Sensor sensor;
    boolean selected;

    public MySensor(Sensor sensor, boolean selected) {
        this.sensor = sensor;
        this.selected = selected;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
