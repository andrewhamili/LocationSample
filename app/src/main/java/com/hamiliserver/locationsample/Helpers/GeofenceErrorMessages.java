package com.hamiliserver.locationsample.Helpers;

import android.content.Context;
import android.content.res.Resources;

public class GeofenceErrorMessages {

    public GeofenceErrorMessages() {
    }

    public static String getErrorString(Context context, int errorCode) {
        Resources resources = context.getResources();

        return String.valueOf(errorCode);
    }
}
