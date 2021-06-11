package com.example.sensorsapp;

import android.hardware.Sensor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SensorViewPagerAdapter extends FragmentPagerAdapter {
    private Fragment fragments[];

    public SensorViewPagerAdapter(FragmentManager fm, int behavior, List<Sensor> sensorsList) {
        super(fm, behavior);
        fragments = new Fragment[sensorsList.size()];
        for (int i = 0; i < sensorsList.size(); i++) {
            fragments[i] = SensorFragment.newInstance(sensorsList.get(i));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Sensor " + (position + 1);
    }
}
