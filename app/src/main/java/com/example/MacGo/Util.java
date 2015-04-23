package com.example.MacGo;

import android.content.Context;
import android.widget.EditText;

import com.parse.ParseObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;

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

    public static void writeDataToStorage(String[] content, Context context){
        String fileName = "MyFile";

        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            outputStream = context.openFileOutput(fileName, context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(content);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] readDataFromStorage(Context context){
        String fileName = "MyFile";
        String[] text = new String[]{"-1","-1","-1"};
        FileInputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        try{
            inputStream = context.openFileInput(fileName);
            objectInputStream = new ObjectInputStream(inputStream);

            text = (String[]) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception ex){

        }

        return text;
    }

    public static void resetData(Context context){
        String fileName = "MyFile";
        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try{
            outputStream = context.openFileOutput(fileName, context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new String[]{"-1", "-1", "-1"});
            outputStream.close();
        }
        catch (Exception ex){

        }
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

    public static void write2file(HashMap<String, String> passcode, Context context) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput("macgo", context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(passcode);
            objectOutputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String,String> readFromFile(Context context){

        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        HashMap passcode;
        try {

            fileInputStream = context.openFileInput("macgo");
            objectInputStream = new ObjectInputStream(fileInputStream);

            passcode = (HashMap) objectInputStream.readObject();
            objectInputStream.close();

            return passcode;
        }catch (Exception ex){
            return null;
        }
    }

    public static boolean updateValueInHash(String key, String value, Context context){
        HashMap<String,String> passcode= readFromFile(context);
        if(passcode.get(key)!=null) {
            passcode.put(key, value);
            return true;
        }
        return false;
    }

}
