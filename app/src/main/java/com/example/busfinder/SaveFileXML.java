package com.example.busfinder;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveFileXML {


    final static String FILENAME="user";

    public static void saveString(Context context, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(FILENAME,0);//mode 0 read and write
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static String getStringValue(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(FILENAME,0);
        return  sp.getString(key,null);//default value is null
    }
    public static void saveInt(Context context, String key, int value){
        SharedPreferences sp = context.getSharedPreferences(FILENAME,0);//mode 0 read and write
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public static int getIntValue(MainActivity mainActivity,String key){
        SharedPreferences sp = mainActivity.getSharedPreferences(FILENAME,0);
        return  sp.getInt(key,0);//default value is null
    }



}
