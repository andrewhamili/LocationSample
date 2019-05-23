package com.hamiliserver.locationsample.Helpers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Util {
    public String callApi(String url, String param, int mode) {
        HttpURLConnection http = null;
        HttpsURLConnection https = null;
        boolean ssl = false;

        String response = "";

        try {
            URL Url = new URL(url);

            if (url.startsWith("https://")) {
                https = (HttpsURLConnection) Url.openConnection();
                https.setRequestProperty("Content-Type", "application/json");
                ssl = true;
            } else {
                http = (HttpURLConnection) Url.openConnection();
                http.setRequestProperty("Content-Type", "application/json");
            }

            // 0 - get, 1 - post
            if (mode == 1) {
                DataOutputStream dStream;

                if (ssl) {
                    https.setReadTimeout(10000);
                    https.setConnectTimeout(10000);
                    https.setRequestMethod("POST");
                    https.setDoOutput(true);
                    https.setDoInput(true);
                    dStream = new DataOutputStream(https.getOutputStream());
                } else {
                    http.setReadTimeout(10000);
                    http.setConnectTimeout(10000);
                    http.setRequestMethod("POST");
                    http.setDoOutput(true);
                    http.setDoInput(true);
                    dStream = new DataOutputStream(http.getOutputStream());
                }

                dStream.writeBytes(param);
                dStream.flush();
                dStream.close();
            }

            int resCode;
            if (ssl) {
                resCode = https.getResponseCode();
            } else {
                resCode = http.getResponseCode();
            }

            if (resCode == 200) {
                String line;

                BufferedReader br;
                if (ssl) {
                    br = new BufferedReader(new InputStreamReader(https.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(http.getInputStream()));
                }

                StringBuilder jsonString = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                br.close();

                response = jsonString.toString();
            } else {
                response = http.getResponseMessage();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (ssl) {
                if (https != null) {
                    https.disconnect();
                }
            } else {
                if (http != null) {
                    http.disconnect();
                }
            }
        }

        return response;
    }
}
