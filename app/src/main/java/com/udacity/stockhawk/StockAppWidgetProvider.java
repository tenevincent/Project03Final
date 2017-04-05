package com.udacity.stockhawk;

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

import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailsActivity;
import com.udacity.stockhawk.widgets.StockAppWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class StockAppWidgetProvider extends AppWidgetProvider {

    private static final int TASK_LOADER_ID = 20;
    private  Context currentContext;



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        this.currentContext = context;

        StockHawkApp stockHawkApp = (StockHawkApp)currentContext.getApplicationContext() ;


        // https://github.com/udacity/Advanced_Android_Development/blob/master/app/src/main/java/com/example/android/sunshine/app/widget/DetailWidgetRemoteViewsService.java




        final int N = appWidgetIds.length;

		/*int[] appWidgetIds holds ids of multiple instance of your widget

		 * meaning you are placing more than one widgets on your homescreen*/

        for (int i = 0; i < N; ++i) {

            RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);

            // Set up the collection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, remoteViews,appWidgetIds[i]);
            } else {
                setRemoteAdapterV11(context, remoteViews);
            }

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views, int appWidgetId) {
        Intent adapter = new Intent(context, StockAppWidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        adapter.setData(Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.listViewWidget, adapter);

    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.listViewWidget,
                new Intent(context, StockAppWidgetService.class));
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

        String action = intent.getAction();
        String actionName = "use_custom_class";

        Log.d("message_tag",action.toString()) ;



     //   Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();


        if (actionName.equals(action)) {
          //  MyClass mc = new MyClass(context);
          //  mc.toggleEnable();
        }


    }


    /*

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
    */
}

