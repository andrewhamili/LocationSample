package com.hamiliserver.locationsample.AsyncTask;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.hamiliserver.locationsample.AsyncTask.Listener.AsyncTaskListener;
import com.hamiliserver.locationsample.Helpers.Util;
import com.hamiliserver.locationsample.Model.APIResponseObject;
import com.hamiliserver.locationsample.R;

import java.util.HashMap;

public class SendLocation extends AsyncTask<Void, Void, Void> {

    Context context;

    Double latitude;

    Double longitude;

    String name = "";

    AsyncTaskListener asyncTaskListener;

    public SendLocation(Context context, Double latitude, Double longitude) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        asyncTaskListener = (AsyncTaskListener) context;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String apiUrl = context.getResources().getString(R.string.apiUrl);

            Util util = new Util();

            HashMap<String, Object> content = new HashMap<>();

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                content.put("imei", tm.getDeviceId());
                content.put("latitude", latitude);
                content.put("longitude", longitude);


                HashMap<String, Object> body = new HashMap<>();

                body.put("content", content);
                body.put("key", "test");

                String jsonBody = new Gson().toJson(body);

                String jsonResponse = util.callApi(apiUrl + "addUser", jsonBody, 1);

                APIResponseObject apiResponseObject = new Gson().fromJson(jsonResponse, APIResponseObject.class);

                if (apiResponseObject.code == 200) {

                    name = apiResponseObject.content.get("name").getAsString();

                }


            }

            return null;


        } catch (Exception e) {

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        asyncTaskListener.updateUser(name);

    }
}
