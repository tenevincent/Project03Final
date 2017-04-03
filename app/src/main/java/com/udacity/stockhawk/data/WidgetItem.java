package com.udacity.stockhawk.data;

/**
 * Created by vince on 03.04.2017.
 */

public class WidgetItem {
    private String symbol;
    private  String price;
    private  String changePercentage;
    private  String changeAbsolute;

    public WidgetItem(String symbol, String price, String changePercentage, String changeAbsolute) {
        this.symbol = symbol;
        this.price = price;
        this.changePercentage = changePercentage;
        this.changeAbsolute = changeAbsolute;
    }

    public String getChangeAbsolute() {
        return changeAbsolute;
    }


    public String getSymbol() {
        return symbol;
    }


    public String getPrice() {
        return price;
    }



    public String getChangePercentage() {
        return changePercentage;
    }






}
