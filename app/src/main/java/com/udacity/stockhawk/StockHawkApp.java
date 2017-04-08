package com.udacity.stockhawk;

import android.app.Application;
import android.database.Cursor;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.WidgetItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import timber.log.Timber;

public class StockHawkApp extends Application {

    private ArrayList<WidgetItem> listItemList = new ArrayList<WidgetItem>();

    public ArrayList<WidgetItem> getListItemList() {

        Collections.sort(listItemList, new Comparator<WidgetItem>() {
            @Override
            public int compare(WidgetItem item1, WidgetItem item2)
            {
                return  item1.getSymbol().compareTo(item2.getSymbol());
            }
        });

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

    public int  fillCursorWithStockData(Cursor cursor) {

        int count = 0;

        if(null == cursor)
            return count;




        int i  = 0; // use to refresh the list only once!

        while (cursor.moveToNext()) {

            if(i == 0){
                this.getListItemList().clear();
            }
            i++;

            String symbol = cursor.getString(Contract.Quote.POSITION_SYMBOL) ;
            String price = cursor.getString(Contract.Quote.POSITION_PRICE) ;
            String changepers = cursor.getString(Contract.Quote.POSITION_PERCENTAGE_CHANGE) ;
            String changeabs = cursor.getString(Contract.Quote.POSITION_ABSOLUTE_CHANGE) ;
            WidgetItem item = new WidgetItem(symbol,price,changepers,changeabs);
            this.getListItemList().add(item) ;
        }
        count = this.getListItemList().size();


        return count;
    }
}
