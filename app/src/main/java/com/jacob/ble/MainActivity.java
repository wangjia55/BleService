package com.jacob.ble;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view){
        Intent intent = new Intent(this,BleService.class);
        startService(intent);
    }

    public void stopService(View view){
        Intent intent = new Intent(this,BleService.class);
        stopService(intent);
    }
}
