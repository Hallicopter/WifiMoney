package com.money.wifi.wifimoney;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;

public class Sell extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        setWifiTetheringEnabled(true);
    }

    public void setWifiTetheringEnabled(boolean enable) {
        //Log.d(TAG,"setWifiTetheringEnabled: "+enable);
        String SSID="WoofWoofHotspotWoof"; // my function is to get a predefined SSID
        String PASS="SpecWoof"; // my function is to get a predefined a Password

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        if(enable){
            wifiManager.setWifiEnabled(!enable);    // Disable all existing WiFi Network
        }else {
            if(!wifiManager.isWifiEnabled())
                wifiManager.setWifiEnabled(!enable);
        }
        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("setWifiApEnabled")) {
                WifiConfiguration netConfig = new WifiConfiguration();
                if(!SSID.isEmpty() || !PASS.isEmpty()){
                    netConfig.SSID=SSID;
                    netConfig.preSharedKey = PASS;
                    netConfig.hiddenSSID = false;
                    netConfig.status = WifiConfiguration.Status.ENABLED;
                    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                }
                try {
                    method.invoke(wifiManager, netConfig, enable);
                    //Log.e(TAG,"set hotspot enable method");
                } catch (Exception ex) {
                }
                break;
            }
        }
    }




}
