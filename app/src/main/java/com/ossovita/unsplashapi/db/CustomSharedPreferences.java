package com.ossovita.unsplashapi.db;

import android.content.Context;
import android.content.SharedPreferences;

public class CustomSharedPreferences {
    private static CustomSharedPreferences instance;
    private static SharedPreferences pref;
    public static CustomSharedPreferences getInstance(Context context){
        if (instance==null){
            instance = new CustomSharedPreferences();
        }
        pref = context.getSharedPreferences("stopwatch",Context.MODE_PRIVATE);
        return instance;
    }

    public void setSearchTerm(String searchTerm){
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("searchTerm",searchTerm).apply();
    }

    public String getSearchTerm(){
        return pref.getString("searchTerm","Nature");
    }
}
