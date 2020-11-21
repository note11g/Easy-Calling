package com.note11.easy_calling.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class NumberCache {

    public static SharedPreferences getShared(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setNumber(Context context, NumberModel model){
        Gson gson = new Gson();
        String json = gson.toJson(model);
        SharedPreferences.Editor editor = getShared(context).edit();
        editor.putString("phone_easy_call",json).apply();
    }

    public static NumberModel getNumber(Context context){
        Gson gson = new Gson();
        return gson.fromJson(getShared(context).getString("phone_easy_call",""), NumberModel.class);
    }

    public static void clear(Context context){
        SharedPreferences.Editor editor = getShared(context).edit();
        editor.putString("phone_easy_call", null).apply();
    }
}
