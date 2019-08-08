package com.liuzhuohui.basemodule.dagger;

import com.liuzhuohui.bluetoothconnection.BluetoothManager;
import com.liuzhuohui.bluetoothconnection.BluetoothManagerImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract  class BluetoothManagerModule {

    @Binds
    abstract BluetoothManager bindBluetoothManager(BluetoothManagerImpl bluetoothManager);
}
