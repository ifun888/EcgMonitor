package com.liuzhuohui.baseservice.service;

import android.content.Context;
import android.util.Log;

import com.liuzhuohui.baseservice.dagger.BtComponents;
import com.liuzhuohui.baseservice.dagger.DaggerBtComponents;
import com.liuzhuohui.bluetoothconnection.BluetoothManager;

import java.lang.annotation.Target;

public class BluetoothService {

    private static final String TAG = "Ecg-Monitor";
    
    private Context mcontext;

    private BluetoothManager bluetoothManager;


    private BluetoothService(){}

    public BluetoothService(Context context){
        this.mcontext = context;

        BtComponents btComponents = DaggerBtComponents.builder()
                .context(this.mcontext)
                .build();
        this.bluetoothManager = btComponents.getBluetoothManager();
    }

    public void scan(){

        bluetoothManager.connectByMac(" ");
    }
}
