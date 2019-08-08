package com.liuzhuohui.baseservice.service;

import android.content.Context;

import com.liuzhuohui.baseservice.dagger.BtComponents;
import com.liuzhuohui.baseservice.dagger.DaggerBtComponents;
import com.liuzhuohui.bluetoothconnection.BluetoothManager;
import com.liuzhuohui.data.BluetoothData;

import io.reactivex.Observable;

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

    public Observable<BluetoothData> connect(){
        return bluetoothManager.connectByMac("4C:49:E3:57:51:63");
    }
}
