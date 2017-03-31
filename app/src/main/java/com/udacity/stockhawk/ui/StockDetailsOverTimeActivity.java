package com.udacity.stockhawk.ui;


import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.CustomValueFormatter;
import com.udacity.stockhawk.data.StockData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class StockDetailsOverTimeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int TASK_LOADER_ID = 20 ;
    private  String mCurrentSymbolKey = null;
    private LineChart lineChart ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details_over_time);

        if(null != getIntent().getExtras()){
            mCurrentSymbolKey = getIntent().getExtras().getString(MainActivity.SYMBOL_KEYWORD) ;
        }
        setActivityTitle();




        lineChart = (LineChart) findViewById(R.id.chart);
        // Y-Right Axis
        lineChart.setDrawBorders(true); // enable the chart border
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setBorderColor(Color.WHITE);

        // Right Axis
        lineChart.getAxisRight().setEnabled(true);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getAxisLeft().setEnabled(true);

        // Sets the legend properties
        lineChart.getLegend().setTextColor(Color.WHITE);
        lineChart.getLegend().setTextSize(11f);
        lineChart.getDescription().setEnabled(false); // chart description disable!


        // lineChart.setGridBackgroundColor(Color.BLUE);
        /*

         Ensure a loader is initialized and active. If the loader doesn't already exist, one is

         created, otherwise the last created loader is re-used.

         */












        YAxis yAxis = lineChart.getAxisLeft();
       // yAxis.setPosition(YAxis.YAxisLabelPosition.LE);
        yAxis.setTextSize(16f);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(true);


        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }


    private void setActivityTitle() {
        String title = mCurrentSymbolKey + getString(R.string.str_stock_over_datetime);
        ActionBar actionbar = this.getSupportActionBar();
        // sets the actionbar
        if(null != actionbar){
            actionbar.setTitle(title);
        }
    }

    private Cursor cursor;
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;
            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(Contract.Quote.URI,
                            null,
                            Contract.Quote.COLUMN_SYMBOL + "=?",
                            new String[]{mCurrentSymbolKey },
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        while (data.moveToNext()) {

            String years = data.getString(data.getColumnIndex(Contract.Quote.COLUMN_HISTORY));


            String[] array = years.split("\n", -1);

            StockData[] dataObjects = new StockData[array.length];

            for (int i = 0; i < array.length; i++){

                String strItem = array[i];
                String[] arrayItem = strItem.split(",", -1);

                if(arrayItem.length < 2){
                    continue;
                }



                 long xpoint1 = Long.parseLong(arrayItem[0]) ;
                float ypoint1 = Float.parseFloat(arrayItem[1]) ;

                StockData stdata = new StockData(xpoint1,ypoint1) ;
                stdata.setIndex(i);
                dataObjects[i] = stdata;
            }





            DoPlotStockHawkData(dataObjects);


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private void DoPlotStockHawkData(StockData[] dataObjects) {



        List<String> values = new ArrayList<>() ;



        List<Entry> entries = new ArrayList<Entry>();
        // fill the entries

        int index = 0;
        for (StockData data : dataObjects) {
            if(null == data){
                continue;
            }
            entries.add(new Entry(data.getIndex(), data.getValueY()));

            //  if(index % 2 == 0){

                Date datetime = new Date(data.getValueX());
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(datetime);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                String date = sdf.format(datetime);
                values.add(date);

         //   }
            index++;
        }

        LineDataSet dataSet = new LineDataSet(entries, mCurrentSymbolKey + "Stock Value"); // add entries to dataset
        dataSet.setColor(Color.WHITE);
        dataSet.setValueTextColor(Color.WHITE); // styling, ...
        dataSet.setLineWidth(1f);
        dataSet.setValueTextSize(12f) ;


        LineData lineData = new LineData(dataSet);
        lineData.setValueTextColor(Color.WHITE);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh


        //
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(16f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);



        String [] astrArray = new String[values.size()];
        for (int i =0; i <values.size() ; i++ ){
            astrArray[i] = values.get(i);
        }

        //display dates
        xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new CustomValueFormatter(astrArray));


        //axis formatter that refers to the LABELS array to put down nice x axis labels

        /*
        IAxisValueFormatter axisValueFormatter = new IAxisValueFormatter() {
            //trying to put in logic to only put labels in certain intervals
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int date_num = (int) value;
                Date date = labels.get(date_num);
                return sdf.format(date);
            }
        };
        XAxis xaxis = lineChart.getXAxis();
        xaxis.setValueFormatter(axisValueFormatter);

        */


    }



}
