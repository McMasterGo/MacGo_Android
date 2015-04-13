package com.example.MacGo;

import android.content.Context;
import android.widget.EditText;

import com.parse.ParseObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
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

    public static void writeDataToStorage(String content, Context context){
        String fileName = "MyFile";

        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(fileName, context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readDataFromStorage(Context context){
        String fileName = "MyFile";
        String text = "";
        FileInputStream inputStream = null;
        try{
            inputStream = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text += line + ",";
            }
        }
        catch (Exception ex){

        }

        return text;
    }

    public static String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }

    public static void changeFocus(EditText from, EditText to){
        from.setFocusable(false);
        to.setFocusable(true);
        to.setFocusableInTouchMode(true);
        to.requestFocus();
    }
}
