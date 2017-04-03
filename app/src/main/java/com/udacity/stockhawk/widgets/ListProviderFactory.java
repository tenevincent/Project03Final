package com.udacity.stockhawk.widgets;

import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.StockHawkApp;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockData;
import com.udacity.stockhawk.data.WidgetItem;

import java.util.ArrayList;

/**
 * Created by vince on 03.04.2017.
 */

public class ListProviderFactory implements RemoteViewsFactory {
    private ArrayList<WidgetItem> listItemList = new ArrayList<WidgetItem>();
    private Context context = null;
    private int appWidgetId;

  //   private Cursor mcursor;


    public ListProviderFactory(Context context, Intent intent) {
        this.context = context;

       appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);


        StockHawkApp stockHawkApp = (StockHawkApp)context.getApplicationContext() ;
        this.listItemList = stockHawkApp.getListItemList();


    //    this.mcursor = context.getContentResolver() ;

      //  populateData();
    }

    /*
    private void populateData() {

        while (mcursor.moveToNext()){
            String symbol = mcursor.getString(Contract.Quote.POSITION_SYMBOL) ;
            String price = mcursor.getString(Contract.Quote.POSITION_PRICE) ;
            String changepers = mcursor.getString(Contract.Quote.POSITION_PERCENTAGE_CHANGE) ;
            String changeabs = mcursor.getString(Contract.Quote.POSITION_ABSOLUTE_CHANGE) ;

            WidgetItem data = new WidgetItem(symbol,price,changepers,changeabs);
            listItemList.add(data) ;
        }
    }

    */

    /*
      *Similar to getView of Adapter where instead of View
      *we return RemoteViews
      *
      */
    @Override
    public RemoteViews getViewAt(int position) {

       final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.list_widget_row);

        WidgetItem listItem = listItemList.get(position);

        remoteView.setTextViewText(R.id.symbol_widget,listItem.getSymbol());
        remoteView.setTextViewText(R.id.price_widget, listItem.getPrice());
        remoteView.setTextViewText(R.id.change_widget, listItem.getPrice());

        return remoteView;
    }


    @Override
    public void onCreate() {


    }

    @Override
    public void onDataSetChanged() {

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
        return false;
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