package com.money.wifi.wifimoney;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class WifiAPController extends Activity {
    public int a;
    public int b;
    public String password;
    public String APname;
    public String SSID;
    public String PSK;

    private static int g;
    private static int h;
    private static int i;
    private static int j;
    private WifiManager wifiManager;
    private String logTAG;
    private int wifiState;
    private boolean o;

    class wifiControllerTask extends AsyncTask {
        WifiAPController wifiAPControllerClass;
        boolean a;
        boolean b;
        Context mContext;

        public wifiControllerTask(WifiAPController wifiAPController, boolean arg3, boolean arg4, Context context) {
            this.wifiAPControllerClass = wifiAPController;
            this.a = arg3;
            this.b = arg4;
            this.mContext = context;
        }

        protected Void a(Void[] arg3) {
            try {
                WifiAPController.wifiToggle(this.wifiAPControllerClass, this.a);
            } catch (Exception v0) {
            }
            return null;
        }

        public void a() {
            int sdkCurrentVersion = 21;
            try {
                if (this.a) {
                    if (Build.VERSION.SDK_INT < sdkCurrentVersion) {
                        return;
                    }

                    this.wifiAPControllerClass.wifiToggle(this.mContext);
                    return;
                }

                if (Build.VERSION.SDK_INT < sdkCurrentVersion) {
                    return;
                }
            } catch (Exception v0) {
                Log.e("noti error", v0.getMessage());
            }
        }

        protected void a(Void arg2) {
            super.onPostExecute(arg2);
            try {
                this.a();
            } catch (IllegalArgumentException v0) {
                try {
                    this.a();
                } catch (Exception v0_1) {
                }
            }

            if (this.b) {
                this.wifiAPControllerClass.finish();
            }
        }

        protected Object doInBackground(Object[] arg2) {
            return this.a(((Void[]) arg2));
        }

        protected void onPostExecute(Object arg1) {
            this.a(((Void) arg1));
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    static {
        WifiAPController.g = 0;
        WifiAPController.h = 0;
        WifiAPController.i = 1;
        WifiAPController.j = 4;
    }

    public WifiAPController() {
        super();
        this.a = 2;
        this.b = 3;
        this.logTAG = "WifiAP";
        this.wifiState = -1;
        this.o = false;
    }

    static int wifiToggle(WifiAPController wifiAPController, boolean wifiToggleFlag) {
        return wifiAPController.wifiToggle(wifiToggleFlag);
    }

    private void initWifiAPConfig(WifiConfiguration wifiConfiguration){
        wifiConfiguration.SSID = SSID;
        wifiConfiguration.preSharedKey = PSK;
        wifiConfiguration.hiddenSSID = false;
        wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfiguration.allowedKeyManagement.set(4);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
    }

    private int wifiToggle(boolean wifiToggleFlag) {
        int wifiState;
        String stateString;
        StringBuilder message;
        long sleepTimeout = 500;
        int maxAttemptCount = 10;
        int errorState = -1;
        Log.d(this.logTAG, "*** setWifiApEnabled CALLED **** " + wifiToggleFlag);
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        initWifiAPConfig(wifiConfiguration);
        if ((wifiToggleFlag) && this.wifiState == errorState) {
            this.wifiState = this.wifiManager.getWifiState();
        }

        if (!(!wifiToggleFlag || this.wifiManager.getConnectionInfo() == null)) {
            Log.d(this.logTAG, "disable wifi: calling");
            this.wifiManager.setWifiEnabled(false);
            int attemptCount = maxAttemptCount;
            while (attemptCount > 0) {
                if (this.wifiManager.getWifiState() == 1) {
                    break;
                }

                Log.d(this.logTAG, "disable wifi: waiting, pass: " + (10 - attemptCount));
                try {
                    Thread.sleep(sleepTimeout);
                    --attemptCount;
                } catch (Exception v4_1) {
                }
            }

            Log.d(this.logTAG, "disable wifi: done, pass: " + (10 - attemptCount));
        }

        try {
            message = new StringBuilder();
            stateString = wifiToggleFlag ? "enabling" : "disabling";
            Log.d(this.logTAG, message.append(stateString).append(" wifi ap: calling").toString());
            Log.d(this.logTAG, this.APname);
            Log.d(this.logTAG, this.password);
            Log.d(this.logTAG, "" + this.wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class).invoke(this.wifiManager, wifiConfiguration, true).toString());
            int res = this.wifiManager.addNetwork(wifiConfiguration);
            Log.d(this.logTAG, "" + res);
            wifiState = (int) this.wifiManager.getClass().getMethod("getWifiApState").invoke(this.wifiManager);
            Log.d(this.logTAG, "" + wifiState);
        } catch (Exception v0_1) {
            Log.e("wifi", v0_1.getMessage());
            wifiState = errorState;
        }

        while (maxAttemptCount > 0) {
            if (this.wifiToggle() != WifiAPController.h && this.wifiToggle() != this.b && this.wifiToggle() != WifiAPController.j) {
                break;
            }
            message = new StringBuilder();
            stateString = wifiToggleFlag ? "enabling" : "disabling";
            Log.d(this.logTAG, message.append(stateString).append(" wifi ap: waiting, pass: ").append(10 - maxAttemptCount).toString());
            sleepTimeout = 500;
            try {
                Thread.sleep(sleepTimeout);
                --maxAttemptCount;
            } catch (Exception v0_1) {
            }
        }
        message = new StringBuilder();
        stateString = wifiToggleFlag ? "enabling" : "disabling";
        Log.d(this.logTAG, message.append(stateString).append(" wifi ap: done, pass: ").append(10 - maxAttemptCount).toString());

        if (!wifiToggleFlag) {
            if ((this.wifiState >= WifiManager.WIFI_STATE_ENABLING && this.wifiState <= WifiManager.WIFI_STATE_UNKNOWN) || (this.o)) {
                Log.d(this.logTAG, "enable wifi: calling");
                this.wifiManager.setWifiEnabled(true);
            }

            this.wifiState = errorState;
            return wifiState;
        }
        return wifiState;
    }

    public int wifiToggle() {
        int result;
        int v4 = 10;
        try {
            result = (int) this.wifiManager.getClass().getMethod("getWifiApState").invoke(this.wifiManager);
        } catch (Exception v0) {
            result = -1;
        }

        if (result >= v4) {
            WifiAPController.g = v4;
        }

        WifiAPController.h = WifiAPController.g;
        WifiAPController.i = WifiAPController.g + 1;
        this.a = WifiAPController.g + 2;
        this.b = WifiAPController.g + 3;
        WifiAPController.j = WifiAPController.g + 4;
        return result;
    }

    public void wifiToggle(Context context) {
        //Intent v0 = new Intent(context, MainActivity.class);
    }

    public void wifiToggle(String apname, String pass, WifiManager wifiManager, Context context) {
        boolean v2 = true;
        if (this.wifiManager == null) {
            this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }

        this.APname = apname;
        this.password = pass;
        this.SSID=apname;
        this.PSK=pass;
        int v0 = this.wifiToggle() == this.b || this.wifiToggle() == this.a ? 1 : 0;
        if (v0 != 0) {
            v2 = false;
        }

        new wifiControllerTask(this, v2, false, context).execute(new Void[0]);
    }
}