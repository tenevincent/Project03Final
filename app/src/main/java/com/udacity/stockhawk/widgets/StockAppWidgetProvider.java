package com.udacity.stockhawk.widgets;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.StockHawkApp;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailsActivity;
import com.udacity.stockhawk.widgets.StockAppWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class StockAppWidgetProvider extends AppWidgetProvider {

    public static final String DATA_FETCHED = "com.color.appwidget.list.UPDATE_LIST";
    // private static final int TASK_LOADER_ID = 20;
    private  Context currentContext;
    public static String UPDATE_LIST = "UPDATE_LIST";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.e("onReceive", "onXonUpdate(1)");

        this.currentContext = context;
        StockHawkApp stockHawkApp = (StockHawkApp)currentContext.getApplicationContext() ;
        final int N = appWidgetIds.length;

		/*int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widgets on your homescreen*/
        for (int i = 0; i < N; ++i) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }



    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        Log.e("onReceive", "onXupdateWidgetListView(1)");



        //which layout to show on widget
        RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.stock_widget_layout);

        Intent svcIntent = new Intent(context,MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,svcIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        widget.setOnClickPendingIntent(R.id.relativ_widget_layout,pendingIntent);

        Intent intent = new Intent(context, StockAppWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));


        // Intent serviceIntent = new Intent(context, StockAppWidgetService.class);
        // serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);



        // widget.setRemoteAdapter(appWidgetId, R.id.listViewWidget, svcIntent);

          widget.setRemoteAdapter( R.id.listViewWidget, intent);
        widget.setPendingIntentTemplate(R.id.listViewWidget, pendingIntent);



        // TODO for notififying the listview item changed
        intent = new Intent(context, StockAppWidgetProvider.class);
        intent.setAction(UPDATE_LIST);
        PendingIntent pendingIntentRefresh = PendingIntent.getBroadcast(context,0, intent, 0);
         widget.setOnClickPendingIntent(R.id.symbol_widget, pendingIntentRefresh);


        /*
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, MainActivity.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);
        */

        context.startService(intent);

        return widget;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // TODO implement update
        Log.e("onReceive", "onXReceive(1)");
        updateWidget(context);
    }


    private void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, StockAppWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);
    }




}

