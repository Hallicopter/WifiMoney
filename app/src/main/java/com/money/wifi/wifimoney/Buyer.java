package com.money.wifi.wifimoney;

        import android.Manifest;
        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.net.wifi.WifiConfiguration;
        import android.net.wifi.WifiManager;
        import android.os.Build;
        import android.provider.Settings;
        import android.support.annotation.RequiresApi;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

        import java.util.ArrayList;
        import java.util.List;
        import android.os.Handler;
        import android.content.Context;

        import android.app.Activity;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.net.wifi.ScanResult;
        import android.net.wifi.WifiManager;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.AdapterView.OnItemClickListener;

public class Buyer extends Activity {


    private StringBuilder sb = new StringBuilder();
    private ListView lv;
    List<ScanResult> scanList;
    WifiManager wifi;
    ArrayList<String> SSIDs;
    MyBroadcastReceiver broadcastReceiver;
    int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        lv=(ListView)findViewById(R.id.lv);
        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else
        {
            scanWifi();
            //do something, permission was previously granted; or legacy device
        }
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                connect(SSIDs.get(myItemInt));
            }
        });

    }

    void connect(String SSID){
        Glob g=new Glob(SSID);
        String password=g.getPassword();
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", SSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", password);


        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//remember id
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
        Intent intent=new Intent(Buyer.this,Rate.class);
        intent.putExtra("Data Cap",g.getData());
        intent.putExtra("Rate",g.getRate());
        startActivity(intent);
    }

    class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent){
            SSIDs=new ArrayList<String>();
            ArrayList<String> arr=new ArrayList<String>();
            List<ScanResult> list=wifi.getScanResults();
            for(ScanResult scan:list){
                try{
                    String ID=scan.SSID;
                    if(ID.substring(0,5).equals("AMBMJ")){
                        Glob g=new Glob(ID);
                        String message="Rate(MB/₹)="+g.getRate()+"  DataLimit(MB)="+g.getData();
                        arr.add(message);
                        SSIDs.add(ID);
                    }
                }catch(Exception e){}
            }
            ArrayAdapter adapter=new ArrayAdapter<String>(Buyer.this, R.layout.text_resource, arr);
            lv.setAdapter(adapter);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do something with granted permission
            scanWifi();
        }
    }

    public void scanWifi() {
        wifi=(WifiManager) getSystemService(WIFI_SERVICE);
        if(!wifi.isWifiEnabled())
            wifi.setWifiEnabled(true);
        broadcastReceiver=new MyBroadcastReceiver();
        registerReceiver(broadcastReceiver,new IntentFilter(wifi.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}
