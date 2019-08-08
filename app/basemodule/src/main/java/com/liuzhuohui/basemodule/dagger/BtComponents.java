package com.liuzhuohui.basemodule.dagger;


import android.content.Context;

import com.liuzhuohui.bluetoothconnection.BluetoothManager;
import com.liuzhuohui.bluetoothconnection.BluetoothManagerImpl;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = BluetoothManagerModule.class)
public interface BtComponents {

    BluetoothManager getBluetoothManager();

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder context(@Named("context")Context context);



        BtComponents build();
    }
}
