package com.hamiliserver.locationsample.AsyncTask;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hamiliserver.locationsample.AsyncTask.Listener.AsyncTaskListener;
import com.hamiliserver.locationsample.Helpers.Util;
import com.hamiliserver.locationsample.Model.APIResponseArray;
import com.hamiliserver.locationsample.R;

import java.util.HashMap;

public class Places extends AsyncTask<Void, Void, Void> {

    Context context;

    Boolean asyncSuccess = false;

    String errorMessaage = "";

    AsyncTaskListener asyncTaskListener;

    public Places(Context context) {
        this.context = context;

        asyncTaskListener = (AsyncTaskListener) context;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Util util = new Util();

        String apiUrl = context.getResources().getString(R.string.apiUrl);

        try {

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                HashMap<String, Object> content = new HashMap<>();

                content.put("imei", tm.getDeviceId());

                HashMap<String, Object> body = new HashMap<>();

                body.put("content", content);
                body.put("key", "test");

                String jsonBody = new Gson().toJson(body);

                String jsonResponse = util.callApi(apiUrl + "getUser", jsonBody, 1);

                APIResponseArray apiResponseArray = new Gson().fromJson(jsonResponse, APIResponseArray.class);

                if (apiResponseArray.code == 200) {

                    com.hamiliserver.locationsample.SharedPreferences.Places places = new com.hamiliserver.locationsample.SharedPreferences.Places();

                    places.placesJsonObject = apiResponseArray.content.toString();

                    com.hamiliserver.locationsample.SharedPreferences.Places.setPreference(context, places);

                    asyncSuccess = true;
                }

            }

        } catch (Exception e) {

            asyncSuccess = false;

            errorMessaage = e.getMessage();

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (!asyncSuccess) {
            Toast.makeText(context, errorMessaage, Toast.LENGTH_SHORT).show();
        }

        asyncTaskListener.updateUI(asyncSuccess);
    }
}
