package com.money.wifi.wifimoney;

        import android.content.ActivityNotFoundException;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.net.TrafficStats;
        import android.os.Handler;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;


public class Rate extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    private long dataCap=0;
    private int rate=0;
    long MB=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();
        Bundle extra=getIntent().getExtras();
        dataCap=extra.getLong("Data Cap");
        rate=extra.getInt("Rate");
        mHandler.postDelayed(mRunnable, 1000);
        Button stop=(Button)findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            public static final String TAG ="";

            @Override
            public void onClick(View v) {

                //add payment function
                double mon=(Math.ceil(((double)MB/rate)/76257));
                String amt=Double.toString(Math.max(mon,0.0000273));
                //String amt="0.000989";


                donateViaBitcoin(amt);
            }
        });
    }

    private void donateViaBitcoin(String amt) {

        String bitcoinSchema = "bitcoin:";
        String bitcoinReceiveAddress = "19JddkKowamVFs3LbpnUcJNyyPp7TL6gWe"; //your address from wallet
        String bitcoinAmount = "?amount=" + amt; //Amount in BTC, 1$
        String bitcoinURI = bitcoinSchema + bitcoinReceiveAddress + bitcoinAmount;
        Intent bitcoinIntent = new Intent(Intent.ACTION_VIEW);
        bitcoinIntent.setData(Uri.parse(bitcoinURI));
        try {
            startActivity(bitcoinIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "You don't have any Bitcoin Wallet installed.", Toast.LENGTH_SHORT).show();
        }
    }
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            TextView RX = (TextView) findViewById(R.id.RX);
            TextView TX = (TextView) findViewById(R.id.TX);
            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
            RX.setText(Long.toString(rxBytes));
            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
            TX.setText(Long.toString(txBytes));
            long total=rxBytes+txBytes;
            MB=(long)Math.ceil(total/1024/1024);
            if(total+(500*1024)>=dataCap)
                //add payment function
                mHandler.postDelayed(mRunnable, 1000);
        }
    };
}
