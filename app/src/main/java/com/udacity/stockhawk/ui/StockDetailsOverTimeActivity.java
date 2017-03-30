package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udacity.stockhawk.R;

public class StockDetailsOverTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details_over_time);

        setActivityTitle();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3)
        });

        series.setTitle("Random Curve 1");
        series.setColor(Color.GREEN);

        graph.addSeries(series);


    }




    private void setActivityTitle() {
        String title = getString(R.string.str_stock_over_datetime);
        ActionBar actionbar = this.getSupportActionBar();
        // sets the actionbar
        if(null != actionbar){
            actionbar.setTitle(title);
        }
    }


}
