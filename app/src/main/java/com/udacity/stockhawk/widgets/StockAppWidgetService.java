package com.udacity.stockhawk.widgets;

import android.annotation.TargetApi;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.StockHawkApp;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.WidgetItem;

import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StockAppWidgetService extends RemoteViewsService {

    private Cursor cursor = null;
    private ArrayList<WidgetItem> listItemList = new ArrayList<WidgetItem>();


    public StockAppWidgetService() {
        //Log.e("dataset2", "StockAppWidgetService(1)");
    }


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

         if (fetchDataFromContentResolver())
             return (new ListProviderFactory(StockAppWidgetService.this, intent));


        return (new ListProviderFactory(this.getApplicationContext(), intent));
    }

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(StockAppWidgetProvider.DATA_FETCHED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        sendBroadcast(widgetUpdateIntent);

        return super.onStartCommand(intent, flags, startId);
    }


    private boolean fetchDataFromContentResolver() {

        try {
            cursor =  this.getContentResolver().query(Contract.Quote.URI,
                    null,
                    null,
                    null,
                    Contract.Quote.COLUMN_SYMBOL + " ASC");


            StockHawkApp stockHawkApp = (StockHawkApp)this.getApplicationContext() ;
            stockHawkApp.fillCursorWithStockData(cursor);
            listItemList = stockHawkApp.getListItemList();
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }



        return false;
    }


}
