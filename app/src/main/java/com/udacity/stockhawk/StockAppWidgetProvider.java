package com.udacity.stockhawk;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViews;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.WidgetItem;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.widgets.StockAppWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class StockAppWidgetProvider extends AppWidgetProvider implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASK_LOADER_ID = 20;
    private  Context currentContext;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        this.currentContext = context;

        StockHawkApp stockHawkApp = (StockHawkApp)currentContext.getApplicationContext() ;


        // activity.getFragmentManager().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);


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

        remoteViews.setOnClickPendingIntent(R.id.linear,pendingIntent);


        Intent intent = new Intent(context, StockAppWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        remoteViews.setRemoteAdapter( R.id.listViewWidget, intent);



        // remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget, intent);






        /*
        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, StockAppWidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        //setting adapter to RecyclerView of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget, svcIntent);



        // remoteViews.setRemoteAdapter( R.id.listViewWidget, svcIntent);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, svcIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.listViewWidget, pendingIntent);


        //setting an empty view in case of no data
         remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);

        */
        return remoteViews;

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        String actionName = "use_custom_class";

        if (actionName.equals(action)) {
          //  MyClass mc = new MyClass(context);
          //  mc.toggleEnable();
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(currentContext) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;
            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                try {
                    return currentContext.getContentResolver().query(Contract.Quote.URI,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        StockHawkApp stockHawkApp = (StockHawkApp)currentContext.getApplicationContext() ;

        while (data.moveToNext()) {
            String symbol = data.getString(Contract.Quote.POSITION_SYMBOL) ;
            String price = data.getString(Contract.Quote.POSITION_PRICE) ;
            String changepers = data.getString(Contract.Quote.POSITION_PERCENTAGE_CHANGE) ;
            String changeabs = data.getString(Contract.Quote.POSITION_ABSOLUTE_CHANGE) ;
            WidgetItem item = new WidgetItem(symbol,price,changepers,changeabs);
            stockHawkApp.getListItemList().add(item) ;
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

