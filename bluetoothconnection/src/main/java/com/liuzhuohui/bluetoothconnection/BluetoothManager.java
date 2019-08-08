package com.liuzhuohui.bluetoothconnection;

import android.bluetooth.BluetoothSocket;

import com.liuzhuohui.data.BluetoothData;
import com.liuzhuohui.data.BluetoothStatusData;

import io.reactivex.Observable;
import io.reactivex.Observer;
import com.liuzhuohui.data.BluetoothData;

// 蓝牙管理器
public interface BluetoothManager {

    Observable<BluetoothData> connectByMac(String mac);//MAC连接
    Observable<BluetoothData> connectByName(String name);//名字连接
    void disconnectByMac(String mac);//断开连接
    void disconnectByName(String name);//断开连接
    Observable<BluetoothStatusData> getStatus(Observer observer);//获取状态
    int writeToBluetooth(byte[] writeBuffer);//写数据

}
