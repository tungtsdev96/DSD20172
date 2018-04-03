package com.hust.edu.dsd;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hust.edu.dsd.utils.Constants;

/**
 * Created by tungts on 9/30/2017.
 */

public class AppPreference {

    public static AppPreference appPreference;
    private static SharedPreferences preferences;
    private Gson mGson;

    private AppPreference(){
        mGson = new GsonBuilder().create();
    }

    public static AppPreference getInstance(Context context){
        if (appPreference == null) appPreference = new AppPreference();
        if (preferences == null) preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return appPreference;
    }

}
