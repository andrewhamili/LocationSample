package com.hamiliserver.locationsample.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Location {

    public static String credentialsStore = "Location";

    public Double latitude;

    public Double longitude;

    public static android.location.Location getLastLocation(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(credentialsStore, Context.MODE_PRIVATE);

        android.location.Location location = new android.location.Location("");

        location.setLatitude(Double.parseDouble(sharedPreferences.getString("latitudeString", "0.0")));

        location.setLongitude(Double.parseDouble(sharedPreferences.getString("longitudeString", "0.0")));

        return location;
    }

    public static void setPreference(Context context, Double latitude, Double longitude) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(credentialsStore, Context.MODE_PRIVATE);

        SharedPreferences.Editor append = sharedPreferences.edit();

        append.putString("latitudeString", Double.toString(latitude));
        append.putString("longitudeString", Double.toString(longitude));

        append.commit();

    }

}
