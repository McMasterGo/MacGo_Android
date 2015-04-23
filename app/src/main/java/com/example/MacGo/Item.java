package com.example.MacGo;

import com.parse.Parse;
import com.parse.ParseObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by KD on 1/12/2015.
 */
public class Item {
    String itemId;
    String itemName;
    Number itemPrice;
    int calories;
    Number itemQuantity;
    private static ParseObject category;

    public final String getItemId() {
        return itemId;
    }

    public Number getPurchaseItemQuantity() {
        return itemQuantity;
    }

    public final String getItemName() {
        return itemName;
    }

    public final Number getItemPrice() {
        return itemPrice;
    }

    public final int getItemCalories() {
        return calories;
    }

    public final ParseObject getItemCategory() {
        return category;
    }

    public static String covertDataFormat(Date date, String format) {
        String formatDate = "00 MMMMM YYYY";
        Format sdf = new SimpleDateFormat(format, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR);
        if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            formatDate = sdf.format(date).toString() + " @"+hours+"pm";

        }
        else {
            formatDate = sdf.format(date).toString() + " @"+hours+"am";
        }
        return formatDate;
    }

    public Item(String itemId, String itemName, Number itemPrice,
                int calories, Number itemQuantity,ParseObject category) {

        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.calories = calories;
        this.category = category;
    }

}
