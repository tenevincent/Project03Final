package com.udacity.stockhawk.data;

/**
 * Created by vince on 31.03.2017.
 */

public class StockData {

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private  int index ;
    private  long valueX;
    private  float valueY;

    public StockData(long valueX, float valueY) {
        this.valueX = valueX;
        this.valueY = valueY;
    }

    public void setValueX(long valueX) {
        this.valueX = valueX;
    }

    public float getValueY() {
        return valueY;
    }

    public void setValueY(float valueY) {
        this.valueY = valueY;
    }

    public long getValueX() {
        return valueX;
    }
}
