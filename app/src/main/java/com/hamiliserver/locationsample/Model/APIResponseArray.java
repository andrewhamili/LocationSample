package com.hamiliserver.locationsample.Model;

import com.google.gson.JsonArray;

public class APIResponseArray {

    public int code;

    public JsonArray content;

    public APIResponseArray() {
        content = new JsonArray();
    }

}
