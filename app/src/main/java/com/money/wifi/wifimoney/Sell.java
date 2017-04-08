package com.money.wifi.wifimoney;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Random;

public class Sell extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        Button sellButton = (Button) findViewById(R.id.sellButton);

        sellButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getSellerDetails(view);
            }
        });
    }
    
    private void getSellerDetails( View view)
    {
        EditText dataInput = (EditText) findViewById(R.id.dataInput);
        EditText timeInput = (EditText) findViewById(R.id.timeInput);
        EditText rateInput = (EditText) findViewById(R.id.rateInput);

        if(isEmpty(dataInput) || isEmpty(timeInput) || isEmpty(rateInput)) {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        int data = Integer.parseInt(dataInput.getText().toString());
        int time = Integer.parseInt(timeInput.getText().toString());
        int rate = Integer.parseInt(rateInput.getText().toString());
        String password = randomString(9);
        Glob sellerGlob = new Glob(password, data, time, rate);
        String SSID = sellerGlob.getSSID();
        Toast.makeText(this, SSID, Toast.LENGTH_LONG).show();
        //createHotspot(SSID, password);
    }

    private boolean isEmpty(EditText etText ) {
        return etText.getText().toString().trim().length() == 0;
    }

    private String randomString(int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
}

class Glob
{
    private String password, SSID;
    private int rate, time, data;
    public String getSSID() {
        return SSID;
    }
    public String getPassword() {
        return password;
    }
    public int getRate() {
        return rate;
    }
    public int getTime() {
        return time;
    }
    public int getData() {
        return data;
    }
    public Glob(String password, int data, int time, int rate )
    {
        this.password = password;
        this.time = time;
        this.data = data;
        this.rate = rate;
        this.SSID = "";
        this.encrypt();
    }

    public Glob(String SSID)
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
        int shift = rate + time + data;

        for(int i = 0 ; i < password.length(); i++)
            SSID += (char) (password.charAt(i) + shift);

        if(SSID.length() % 2 != 0)
            SSID += '.';

        SSID = "AMBMJ" + rate + SSID.substring(0,SSID.length()/2) + time + SSID.substring(SSID.length()/2) + data;
    }
    private void decrypt() {
        SSID = SSID.substring(5);
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