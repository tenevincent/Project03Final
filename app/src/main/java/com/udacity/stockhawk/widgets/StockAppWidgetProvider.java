package com.udacity.stockhawk.widgets;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
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

    // private static final int TASK_LOADER_ID = 20;
    private  Context currentContext;



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        this.currentContext = context;

        StockHawkApp stockHawkApp = (StockHawkApp)currentContext.getApplicationContext() ;
        final int N = appWidgetIds.length;

		/*int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widgets on your homescreen*/
        for (int i = 0; i < N; ++i) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }



    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stock_widget_layout);

        Intent openIntent = new Intent(context,MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,openIntent,0);

        remoteViews.setOnClickPendingIntent(R.id.relativ_widget_layout,pendingIntent);

        Intent intent = new Intent(context, StockAppWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        remoteViews.setRemoteAdapter( R.id.listViewWidget, intent);
        remoteViews.setPendingIntentTemplate(R.id.listViewWidget, pendingIntent);

        return remoteViews;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        /*
        String action = intent.getAction();
        String actionName = "use_custom_class";
        Log.d("message_tag",action.toString()) ;
        if (actionName.equals(action)) {
          //  MyClass mc = new MyClass(context);
          //  mc.toggleEnable();
        }
        */

    }



}

