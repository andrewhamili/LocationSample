package com.hamiliserver.locationsample.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Places {

    public static String credentialsStore = "Places";

    public String placesJsonObject = "";

    public static String getPlacesJsonObject(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(credentialsStore, Context.MODE_PRIVATE);

        return sharedPreferences.getString("placesJsonObject", "[]");

    }

    public static void setPreference(Context context, Places param) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(credentialsStore, Context.MODE_PRIVATE);

        SharedPreferences.Editor append = sharedPreferences.edit();

        append.putString("placesJsonObject", param.placesJsonObject);

        append.commit();

    }

}
