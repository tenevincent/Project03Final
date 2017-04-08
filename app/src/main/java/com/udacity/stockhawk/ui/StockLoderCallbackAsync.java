package com.udacity.stockhawk.ui;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.udacity.stockhawk.StockHawkApp;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.WidgetItem;
import com.udacity.stockhawk.widgets.StockAppWidgetService;

/**
 * Created by vince on 03.04.2017.
 */
public class StockLoderCallbackAsync implements LoaderManager.LoaderCallbacks<Cursor> {

    public StockLoderCallbackAsync(Context currentContext) {
        this.currentContext = currentContext;
    }

    private Context currentContext ;


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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        StockHawkApp stockHawkApp = (StockHawkApp)currentContext.getApplicationContext() ;
        int size = stockHawkApp.fillCursorWithStockData(cursor);

        // Log.e("dataset", "onXonLoadFinished(1)" + " ITEM SIZE: " + size );

        startStockDetailsService();
    }



    private void startStockDetailsService() {
        Intent service = new Intent(currentContext,StockAppWidgetService.class);
        currentContext.startService(service) ;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {



    }

}