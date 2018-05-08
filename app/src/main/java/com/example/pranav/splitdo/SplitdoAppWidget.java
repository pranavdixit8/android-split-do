package com.example.pranav.splitdo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class SplitdoAppWidget extends AppWidgetProvider {

    public static final String TAG =  SplitdoAppWidget.class.getSimpleName();


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,int count, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.pending_tasks);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.splitdo_app_widget);
        views.setTextViewText(R.id.tv_pending_tasks, widgetText);

        views.setTextViewText(R.id.tv_pending_count, count +"");


        Intent intent =  new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        views.setOnClickPendingIntent(R.id.tv_pending_tasks,pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        SplitdoIntentService.updateWithCount(context);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.tv_pending_count);


    }

    public static void updateTasksWidgets(Context context, AppWidgetManager appWidgetManager,
                                          int count, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, count, appWidgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.tv_pending_count);
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

