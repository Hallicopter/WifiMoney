package com.money.wifi.wifimoney;

public class Glob
{
    private String password, SSID;
    private int rate, data, shift;
    public String getSSID() {
        return SSID;
    }
    public String getPassword() {
        return password;
    }
    public int getRate() {
        return rate;
    }
    public int getData() {
        return data;
    }
    public Glob(String password, int data, int rate ) {
        this.password = password;
        this.data = data;
        this.rate = rate;
        this.SSID = "";
        this.shift = getShift();
        this.encrypt();
    }

    public Glob(String SSID) {
        this.SSID = SSID;
        this.password = "";
        this.data = 0;
        this.rate = 0;
        this.decrypt();
    }

    private void encrypt() {
        for(int i = 0 ; i < password.length(); i++)
            SSID += (char) (password.charAt(i) + shift);

        if(SSID.length() % 2 != 0)
            SSID += '.';

        SSID = "AMBMJ" + rate + SSID + data;
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

        shift = getShift();
        int len = SSID.length();
        if(SSID.charAt(len - 1) == '.'){
            len--;
        }
        for(int i = 0; i < len; i++) {
            password += (char) (SSID.charAt(i) - shift);
        }
    }

    private int getShift(){
        return (data + rate) % 5;
    }
}