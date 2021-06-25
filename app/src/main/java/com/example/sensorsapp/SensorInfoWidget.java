package com.example.sensorsapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class SensorInfoWidget extends AppWidgetProvider {
    private SharedPreferences sharedPreferences;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sensor_info_widget);

        // get sp values: current widget index & all the sensors info
        int widgetIndex = Integer.parseInt(sharedPreferences.getString("widget_index", null));
        String[] all_sensors_info = sharedPreferences.getString("all_sensors_info", null).split(">>>");

        // increment the current widget index whenever this method is called and update sp
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(++widgetIndex >= all_sensors_info.length) widgetIndex = 0;
        editor.putString("widget_index", String.valueOf(widgetIndex));
        editor.apply();

        // get current widget sensor info
        String[] sensor_info = all_sensors_info[widgetIndex].split("///");
        CharSequence[] widgetText = new CharSequence[6];
        for (int i = 0; i < 6; i++)
            widgetText[i] = sensor_info[i];

        // update views
        views.setTextViewText(R.id.sensor_name_widget, "Name: " + widgetText[0]);
        views.setTextViewText(R.id.sensor_vendor_widget, "Vendor: " + widgetText[1]);
        views.setTextViewText(R.id.sensor_version_widget, "Version: " + widgetText[2]);
        views.setTextViewText(R.id.sensor_type_widget, "Type: " + widgetText[3]);
        views.setTextViewText(R.id.sensor_power_widget, "Power: " + widgetText[4]);
        views.setTextViewText(R.id.sensor_maxrange_widget, "Max. Range: " + widgetText[5]);

        // pending intent
        int[] idArray = new int[]{appWidgetId};
        Intent intentUpdate = new Intent(context, SensorInfoWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button_next, pendingUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // get Main Activity sp
        sharedPreferences = context.getSharedPreferences(MainActivity.SP_NAME, Context.MODE_PRIVATE);

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}