package com.money.wifi.wifimoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Button sell = (Button) findViewById(R.id.sellbtn);

        sell.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                Intent click = new Intent(SelectActivity.this,Sell.class);
                startActivity(click);
            }


        });

        Button buy = (Button) findViewById(R.id.buybtn);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectActivity.this,Buyer.class);
                startActivity(intent);
            }
        });

    }

}
