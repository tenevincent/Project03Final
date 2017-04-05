package com.udacity.stockhawk.widgets;

import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.StockHawkApp;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.WidgetItem;
import com.udacity.stockhawk.ui.StockLoderCallbackAsync;

public class StockAppWidgetService extends RemoteViewsService {

    private Cursor data = null;


    public StockAppWidgetService() {
    }

    StockLoderCallbackAsync loadCallback;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


        try {
            data =  this.getContentResolver().query(Contract.Quote.URI,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        StockHawkApp stockHawkApp = (StockHawkApp)this.getApplicationContext() ;

        if(null == data){
            return (new ListProviderFactory(this.getApplicationContext(), intent));
        }

        stockHawkApp.fillCursorWithStockData(data);

        return (new ListProviderFactory(this.getApplicationContext(), intent));



    }


}
