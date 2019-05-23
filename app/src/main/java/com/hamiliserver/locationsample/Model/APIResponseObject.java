package com.hamiliserver.locationsample.Model;

import com.google.gson.JsonObject;

public class APIResponseObject {

    public int code;

    public JsonObject content;

    public APIResponseObject() {
        content = new JsonObject();
    }

}
