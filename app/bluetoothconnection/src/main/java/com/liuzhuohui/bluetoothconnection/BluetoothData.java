package com.liuzhuohui.bluetoothconnection;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

public  class BluetoothData {

    private static BluetoothData bluetoothData;

    private static boolean Valided;
    private static boolean Opened;
    private static boolean Connected;
    private static List<BluetoothDevice> bondDevices;
    private static List<BluetoothDevice> connectableDevices;
    private static boolean Discoverying;//是否正在扫描

    public static boolean isDiscoverying() {
        return Discoverying;
    }

    public static void setDiscoverying(boolean isDiscoverying) {
        BluetoothData.Discoverying = isDiscoverying;
    }

    public static boolean isValided() {
        return Valided;
    }

    public static void setValided(boolean valided) {
        Valided = valided;
    }

    public static boolean isOpened() {
        return Opened;
    }

    public static void setOpened(boolean opened) {
        Opened = opened;
    }

    public static boolean isConnected() {
        return Connected;
    }

    public static void setConnected(boolean connected) {
        Connected = connected;
    }

    public static List<BluetoothDevice> getBondDevices() {
        return bondDevices;
    }

    public static void setBondDevices(List<BluetoothDevice> bondDevices) {
        BluetoothData.bondDevices = bondDevices;
    }

    public static List<BluetoothDevice> getConnectableDevices() {
        return connectableDevices;
    }

    public static void setConnectableDevices(List<BluetoothDevice> connectableDevices) {
        BluetoothData.connectableDevices = connectableDevices;
    }

    static {
        Valided = false;
        Opened = false;
        Connected = false;
        bondDevices = new ArrayList<BluetoothDevice>();
        connectableDevices = new ArrayList<BluetoothDevice>();
        bluetoothData = new BluetoothData();
    }


    private BluetoothData(){}

//  获取对象
    public static BluetoothData getInstance(){
        return bluetoothData;
    }
}
