package com.liuzhuohui.bluetoothconnection;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

// 蓝牙管理器
public interface BluetoothManager {

    void connectByMac(String mac);//MAC连接
    void connectByName(String name);//名字连接
    void disconnectByMac(String mac);//断开连接
    void disconnectByName(String name);//断开连接
    Observable<BluetoothData> getStatus(Observer observer);//获取状态

}
