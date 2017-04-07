package com.udacity.stockhawk;

import android.app.Application;
import android.database.Cursor;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.WidgetItem;

import java.util.ArrayList;

import timber.log.Timber;

public class StockHawkApp extends Application {

    private ArrayList<WidgetItem> listItemList = new ArrayList<WidgetItem>();

    public ArrayList<WidgetItem> getListItemList() {
        return listItemList;
    }

    public void setListItemList(ArrayList<WidgetItem> listItemList) {
        this.listItemList = listItemList;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());
        }
    }

    public void fillCursorWithStockData(Cursor cursor) {
        if(null == cursor)
            return;

        this.getListItemList().clear();

        while (cursor.moveToNext()) {
            String symbol = cursor.getString(Contract.Quote.POSITION_SYMBOL) ;
            String price = cursor.getString(Contract.Quote.POSITION_PRICE) ;
            String changepers = cursor.getString(Contract.Quote.POSITION_PERCENTAGE_CHANGE) ;
            String changeabs = cursor.getString(Contract.Quote.POSITION_ABSOLUTE_CHANGE) ;
            WidgetItem item = new WidgetItem(symbol,price,changepers,changeabs);
            this.getListItemList().add(item) ;
        }
    }
}
