package com.money.wifi.wifimoney;

import android.content.*;
import android.net.wifi.*;
import android.util.Log;

import java.lang.reflect.*;

import static android.content.ContentValues.TAG;

public class ApManager {

    //check whether wifi hotspot on or off
    public static boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Throwable ignored) {}
        return false;
    }

    // toggle wifi hotspot on or off
    public static boolean configApState(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            // if WiFi is on, turn it off
            if(isApOn(context)) {
                wifimanager.setWifiEnabled(false);
                Log.e(TAG, "Switched off wifi");
            }
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, !isApOn(context));
            Log.e(TAG, "Passes loles");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failes lols");
        }
        return false;
    }
}