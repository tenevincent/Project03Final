package com.udacity.stockhawk.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.ui.StockLoderCallbackAsync;

public class StockAppWidgetService extends RemoteViewsService {
    public StockAppWidgetService() {
    }

    StockLoderCallbackAsync loadCallback;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        /*
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        */

      //  loadCallback = new StockLoderCallbackAsync(this.getApplicationContext()) ;



        return (new ListProviderFactory(this.getApplicationContext(), intent));


    }


}
