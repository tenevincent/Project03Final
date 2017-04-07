package com.udacity.stockhawk.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import android.widget.Toast;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.StockHawkApp;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.WidgetItem;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailsActivity;

import java.util.ArrayList;

/**
 * Created by vince on 03.04.2017.
 */

public class ListProviderFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<WidgetItem> listItemList = new ArrayList<WidgetItem>();
    private Context context = null;
    private int appWidgetId;
    private Cursor cursor;

    // public static final String ACTION_EXTRA_ITEM = "ACTION_EXTRA_ITEM_KEY";

  //   private Cursor mcursor;


    public ListProviderFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);


        StockHawkApp stockHawkApp = (StockHawkApp)context.getApplicationContext() ;
        this.listItemList = stockHawkApp.getListItemList();

        Log.e("onReceive", "onXListProviderFactory(1): " + this.listItemList.size() + " items");


        /*
        try {
            cursor = context.getContentResolver().query(Contract.Quote.URI,
                    null,
                    null,
                    null,
                    null);
            stockHawkApp.fillCursorWithStockData(cursor);
            this.listItemList = stockHawkApp.getListItemList();
        } catch (Exception e){
            e.printStackTrace();
        }
        */


    }




    /*
      *Similar to getView of Adapter where instead of View
      *we return RemoteViews
      *
      */
    @Override
    public RemoteViews getViewAt(int position) {


        final RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.list_widget_row);

        WidgetItem item = listItemList.get(position);
        view.setTextViewText(R.id.symbol_widget,item.getSymbol());
        view.setTextColor(R.id.price_widget, Color.WHITE);
        view.setTextViewText(R.id.price_widget, item.getPrice());
        view.setTextColor(R.id.price_widget, Color.WHITE);
        view.setTextViewText(R.id.change_widget, item.getChangePercentage());


        float rawAbsoluteChange = Float.parseFloat(item.getChangeAbsolute());

        if (rawAbsoluteChange > 0) {
            view.setTextColor(R.id.change_widget, Color.GREEN);
            Bitmap stockbitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.stock_up);
            view.setImageViewBitmap(R.id.imageview_widget_stock_updown,stockbitMap);
        } else {
            view.setTextColor(R.id.change_widget, Color.RED);
            Bitmap stockbitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.stock_down);
            view.setImageViewBitmap(R.id.imageview_widget_stock_updown,stockbitMap);
        }


        Bundle extras = new Bundle();
       // extras.putInt(ACTION_EXTRA_ITEM, position);
        String data= item.getSymbol();
        Intent intent = new Intent(context,StockDetailsActivity.class);
        intent.putExtra(context.getString(R.string.str_stock_keyword),data);
        intent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        view.setOnClickFillInIntent(R.id.price_widget, intent);
        view.setOnClickFillInIntent(R.id.change_widget, intent);
        view.setOnClickFillInIntent(R.id.symbol_widget, intent);
        view.setOnClickFillInIntent(R.id.textview_placeholder, intent);
        view.setOnClickFillInIntent(R.id.imageview_widget_stock_updown, intent);


        Log.e("onReceive", "onXgetViewAt(1)");


        /*
        // create a pending activity and associated a previously created intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        view.setOnClickPendingIntent(R.id.price_widget,pendingIntent);
        view.setOnClickPendingIntent(R.id.change_widget,pendingIntent);
        view.setOnClickPendingIntent(R.id.symbol_widget,pendingIntent);
        view.setOnClickPendingIntent(R.id.textview_placeholder,pendingIntent);
        view.setOnClickPendingIntent(R.id.imageview_widget,pendingIntent);
*/
        return view;
    }




    @Override
    public void onCreate() {
        StockHawkApp stockHawkApp = (StockHawkApp)context.getApplicationContext() ;
        this.listItemList = stockHawkApp.getListItemList();

        Log.e("dataset", "onXonCreate(1)");

    }

    @Override
    public void onDataSetChanged() {

        if(cursor != null){
            cursor.close();
        }


        Log.e("dataset", "onXDataSetChanged(1)");


        try {


            StockHawkApp stockHawkApp = (StockHawkApp)context.getApplicationContext() ;
            this.listItemList = stockHawkApp.getListItemList();

            /*
            cursor = context.getContentResolver().query(Contract.Quote.URI,
                    null,
                    null,
                    null,
                    null);

            StockHawkApp stockHawkApp = (StockHawkApp)context.getApplicationContext() ;
            stockHawkApp.fillCursorWithStockData(cursor);
            this.listItemList = stockHawkApp.getListItemList();

            */


        } catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


}