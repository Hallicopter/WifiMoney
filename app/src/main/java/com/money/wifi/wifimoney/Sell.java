package com.money.wifi.wifimoney;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Method;

public class Sell extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        HotspotDataWrite();
    }

    private void HotspotDataWrite() {
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);

        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }


        WifiConfiguration netConfig = new WifiConfiguration();


        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        netConfig.SSID = "EASi";
        netConfig.preSharedKey = "Sharath";


        try{

            Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            boolean apstatus=(Boolean) setWifiApMethod.invoke(wifiManager, netConfig,true);

            Method isWifiApEnabledmethod = wifiManager.getClass().getMethod("isWifiApEnabled");
            while(!(Boolean)isWifiApEnabledmethod.invoke(wifiManager)){};

            Method getWifiApStateMethod = wifiManager.getClass().getMethod("getWifiApState");
            int apstate=(Integer)getWifiApStateMethod.invoke(wifiManager);

            Method getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            netConfig=(WifiConfiguration)getWifiApConfigurationMethod.invoke(wifiManager);

            Log.i("Writing HotspotData", "\nSSID:"+netConfig.SSID+"\nPassword:"+netConfig.preSharedKey+"\n");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getSellerDetails( View view) {
        EditText dataInput = (EditText) findViewById(R.id.dataInput);
        int data = Integer.parseInt(dataInput.getText().toString());

        EditText timeInput = (EditText) findViewById(R.id.timeInput);
        int time = Integer.parseInt(timeInput.getText().toString());

        EditText rateInput = (EditText) findViewById(R.id.rateInput);
        int rate = Integer.parseInt(rateInput.getText().toString());

        Glob sellerGlob = new Glob(data, time, rate);
        //AMSBMJ
        String SSID = sellerGlob.SSID;


    }
}

class Glob
{
    String password, SSID;
    int rate, time, data;
    protected Glob(int data, int time, int rate )
    {
        this.password = password;
        this.data = data;
        this.rate = rate;
        this.SSID = "";
        this.encrypt();
    }

    protected Glob(String SSID)
    {
        this.SSID = SSID;
        this.password = "";
        this.data = 0;
        this.rate = 0;
        this.time = 0;
        this.decrypt();
    }

    private void encrypt()
    {
        SSID = "";
        int shift = rate + time + data;

        for(int i = 0 ; i < password.length(); i++)
            SSID += (char) (password.charAt(i) + shift);

        if(SSID.length() % 2 != 0)
            SSID += '.';

        SSID = rate + SSID.substring(0,SSID.length()/2) + time + SSID.substring(SSID.length()/2) + data;
    }
    private void decrypt() {
        for(int i = 0; i < SSID.length(); i++) {
            if(!Character.isDigit(SSID.charAt(i))) {
                rate = Integer.parseInt(SSID.substring(0, i));
                SSID = SSID.substring(i);
                break;
            }
        }

        for(int i = SSID.length() - 1; i >= 0; i--) {
            if(!Character.isDigit(SSID.charAt(i))) {
                data = Integer.parseInt(SSID.substring(i+1));
                SSID = SSID.substring(0, i+1);
                break;
            }
        }

        for(int i = 0; i < SSID.length(); i++) {
            if(Character.isDigit(SSID.charAt(i))) {
                int j;
                for(j = i+1; Character.isDigit(SSID.charAt(j)); j++);
                time = Integer.parseInt(SSID.substring(i, j));
                SSID = SSID.substring(0, i) + SSID.substring(j);
                break;
            }
        }

        int shift = time + rate + data;
        int len = SSID.length();
        if(SSID.charAt(len - 1) == '.'){
            len--;
        }
        for(int i = 0; i < len; i++) {
            password += (char) (SSID.charAt(i) - shift);
        }
    }
}