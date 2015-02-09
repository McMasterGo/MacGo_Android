package com.example.MacGo;

import com.parse.Parse;
import com.parse.ParseObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by KD on 1/12/2015.
 */
public class Item {
    String itemId;
    String itemName;
    float itemPrice;
    int calories;
    private static ParseObject category;

    public final String getItemId() {
        return itemId;
    }

    public final String getItemName() {
        return itemName;
    }

    public final float getItemPrice() {
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
        formatDate = sdf.format(date).toString();
        return formatDate;
    }

    public Item(String itemId, String itemName, float itemPrice,
                int calories, ParseObject category) {

        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.calories = calories;
        this.category = category;
    }

}
