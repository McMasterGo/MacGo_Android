package com.example.MacGo;

import com.parse.ParseObject;

import java.text.DecimalFormat;

/**
 * Created by KD on 1/17/2015.
 */
public class Util {
    private static ParseObject test ;

    public static void setObject(ParseObject p) {
        test = p;

    }

    public static ParseObject getParseObject() {
        return test;
    }

    public static String getDesiredDecimalPrecesion(Number number){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(number);
    }
}
