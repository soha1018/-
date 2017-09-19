package com.example.traffic8_29.Http;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.traffic8_29.Bean.BaseBean;
import com.example.traffic8_29.Global.MyApplication;
import com.example.traffic8_29.Utils.SpUtils;

import org.json.JSONObject;

import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Administrator on 2017/8/29.
 */

public abstract class HttpQuery {
    private int METHODM = Request.Method.POST;

    public HttpQuery(String par, Map<String, Object> map) {
        String ip = (String) SpUtils.getValues(MyApplication.getContext(), Https.IP_ADDRESS, "");
        String port = (String) SpUtils.getValues(MyApplication.getContext(), Https.PORT, "");
        if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
            return;
        } else {
            Https.ip = ip;
            Https.port = port;
        }
        String url = "http://" + Https.ip + ":" + Https.port + "/transportservice/action/" + par + ".do";
        Log.i(TAG, "HttpQuery: url地址是：" + url);
        MyApplication.getRequestQueue().cancelAll(par);
        JSONObject jsonObject = null;
        if (map != null) {
            jsonObject = new JSONObject(map);
        }
        JsonObjectRequest request = new JsonObjectRequest(METHODM, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i(TAG, "onResponse: " + jsonObject.toString());
                BaseBean baseBean = JSONUtils.getValues(jsonObject.toString());
                onSucceed(baseBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onError(volleyError.toString());
            }
        });

        MyApplication.getRequestQueue().add(request);
    }

    public abstract void onSucceed(BaseBean baseBean);

    public abstract void onError(String s);
}
