package com.udacity.stockhawk;

import android.app.Application;

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
}
