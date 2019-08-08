package com.liuzhuohui.ecg_monitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.liuzhuohui.baseservice.service.BluetoothService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothService bluetoothService = new BluetoothService(this);
        bluetoothService.scan();

    }
}
