package com.udacity.stockhawk.data;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by vince on 31.03.2017.
 */

public class CustomValueFormatter implements IAxisValueFormatter {
    private String[] mValues;
    public CustomValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String format = "";
        try{
            int index = (int) value ; // skip each even index
            if(index % 2 != 0){
                return "";
            }
            format = mValues[index];
        }catch (Exception ex){
        }
       return format;

    }
}
