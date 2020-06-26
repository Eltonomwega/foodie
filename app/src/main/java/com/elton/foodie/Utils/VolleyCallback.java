package com.elton.foodie.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyCallback {

    void onSuccess(JSONObject jsonObject) throws JSONException;
    void onSuccess2(JSONArray jsonArray)throws JSONException;
    void onSuccess(JSONArray jsonArray) throws JSONException;


}
