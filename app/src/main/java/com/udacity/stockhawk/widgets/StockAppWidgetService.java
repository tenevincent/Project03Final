package com.udacity.stockhawk.widgets;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.StockHawkApp;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.WidgetItem;

import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StockAppWidgetService extends RemoteViewsService {

    private Cursor data = null;
    private ArrayList<WidgetItem> listItemList = new ArrayList<WidgetItem>();


    public StockAppWidgetService() {

    }


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
            return (new ListProviderFactory(StockAppWidgetService.this, intent));
        }
        stockHawkApp.fillCursorWithStockData(data);
        listItemList = stockHawkApp.getListItemList();


        /*
        return new RemoteViewsFactory() {
            @Override
            public void onCreate() {
                if (data != null) {
                    data.close();
                }
            }

            @Override
            public void onDataSetChanged() {

            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews view = new RemoteViews(getPackageName(),
                        R.layout.list_widget_row);

                WidgetItem listItem = listItemList.get(position);

                view.setTextViewText(R.id.symbol_widget,listItem.getSymbol());
                view.setTextColor(R.id.price_widget, Color.WHITE);
                view.setTextViewText(R.id.price_widget, listItem.getPrice());
                view.setTextColor(R.id.price_widget, Color.WHITE);
                view.setTextViewText(R.id.change_widget, listItem.getChangePercentage());


                Bundle extras = new Bundle();
                extras.putInt(ListProviderFactory.ACTION_EXTRA_ITEM, position);
                String data= listItem.getSymbol();
                Intent fillInIntent = new Intent();
                fillInIntent.putExtra("homescreen_meeting2",data);
                fillInIntent.putExtras(extras);
                // Make it possible to distinguish the individual on-click
                // action of a given item
                view.setOnClickFillInIntent(R.layout.list_widget_row, fillInIntent);

                return  view;

            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            private Cursor data = null;



        };

        */


        return (new ListProviderFactory(StockAppWidgetService.this, intent));

    }






}
