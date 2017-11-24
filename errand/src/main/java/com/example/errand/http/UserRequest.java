package com.example.errand.http;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.errand.global.MyApplication;
import com.example.errand.intafece.UserInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/11/23.
 */

public class UserRequest {
    private static final String URL = "172.16.7.151:8080";
    private static final String TAG = "UserRequest";
    private static final int METHOD = Request.Method.POST;
    private static final String REGISTER_URL = "http://"+URL+"/Shop/register";
    private static final String LOGIN_URL = "http://"+URL+"/Shop/login";

    /**
     * 用户注册
     */
    public void registerUser(String username, String password, String email, String phone, final UserInterface userInterface) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
            jsonObject.put("telephone", phone);

            JsonObjectRequest request = new JsonObjectRequest(METHOD, REGISTER_URL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.i(TAG, "onResponse: " + jsonObject.toString());
                        String success = jsonObject.getString("success");
                        Log.i(TAG, "onResponse: " + success);
                        if (success.equals("ok")) {
                            userInterface.onSuccess();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    userInterface.onError();
                    Log.i(TAG, "onErrorResponse: " + volleyError.toString());
                }
            });

            MyApplication.getRequestQueue().add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 用户登录
     */
    public void login(String username, String password, final UserInterface userInterface) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            Log.i(TAG, "login: "+LOGIN_URL);
            JsonObjectRequest request = new JsonObjectRequest(METHOD, LOGIN_URL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    String success = null;
                    try {
                        success = jsonObject.getString("success");
                        Log.i(TAG, "onResponse: " + success);
                        if (success.equals("ok")) {
                            userInterface.onSuccess();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    userInterface.onError();
                }
            });
            MyApplication.getRequestQueue().add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}