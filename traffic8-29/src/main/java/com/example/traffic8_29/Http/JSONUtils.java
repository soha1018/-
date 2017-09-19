package com.example.traffic8_29.Http;

import com.example.traffic8_29.Bean.BaseBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/29.
 */

public class JSONUtils {
    public static BaseBean getValues(String values) {
        JSONObject jsonObject = null;
        BaseBean bean = null;
        try {
            jsonObject = new JSONObject(values);
            bean = new BaseBean();
            if (jsonObject.has("RESULT")) {
                bean.isSucceed = jsonObject.optString("RESULT").equals("S");
            }
            if (jsonObject.has("pm2.5")) {
                bean.pm25 = jsonObject.optInt("pm2.5");
            }
            if (jsonObject.has("co2")) {
                bean.co2 = jsonObject.optInt("co2");
            }
            if (jsonObject.has("humidity")) {
                bean.humidity = jsonObject.optInt("humidity");
            }
            if (jsonObject.has("temperature")) {
                bean.temp = jsonObject.optInt("temperature");
            }
            if (jsonObject.has("LightIntensity")) {
                bean.light = jsonObject.optInt("LightIntensity");
            }
            if (jsonObject.has("Balance")) {
                bean.balance = jsonObject.optInt("Balance");
            }
            if (jsonObject.has("RedTime")) {
                bean.redTime = jsonObject.optInt("RedTime");
            }
            if (jsonObject.has("GreenTime")) {
                bean.greedTime = jsonObject.optInt("GreenTime");
            }
            if (jsonObject.has("YellowTime")) {
                bean.yellowTime = jsonObject.optInt("YellowTime");
            }
            if (jsonObject.has("Balance")) {
                bean.balance = jsonObject.optInt("Balance");
            }
            if (jsonObject.has("CarSpeed")) {
                bean.carSpeed = jsonObject.optInt("CarSpeed");
            }
            if (jsonObject.has("Status")) {
                bean.lightStatus = jsonObject.optString("Status");
            }
            if (jsonObject.has("LightIntensity")) {
                bean.lightIntensity = jsonObject.optInt("LightIntensity");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean;
    }
}
