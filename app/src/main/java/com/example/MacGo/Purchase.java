package com.example.MacGo;

import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by KD on 1/12/2015.
 */
public class Purchase {
    ParseObject purchaseId;
    String purchaseDescription;
    Number purchaseCost;
    Date purchaseDate;
    public final String getPurchaseDescription() {
        return purchaseDescription;
    }

    public Number getPurchaseCost() {
        return purchaseCost;
    }

    public final String getPurchaseDateString(String outputFormat) {
        String formatDate = "00 MMMMM YYYY";
        Format sdf = new SimpleDateFormat(outputFormat, Locale.US);
        formatDate = sdf.format(this.purchaseDate).toString();
        return formatDate;
    }

    public final Date getPurchaseDate() {
        return purchaseDate;
    }

    public Purchase(ParseObject purchaseId, String purchaseDescription,
                    Number purchaseCost, Date purchaseDate) {

        this.purchaseId = purchaseId;
        this.purchaseDescription = purchaseDescription;
        this.purchaseCost = purchaseCost;
        this.purchaseDate = purchaseDate;
    }
}



