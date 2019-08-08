package com.liuzhuohui.data;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public  class BluetoothStatusData {

    private static BluetoothStatusData bluetoothStatusData;

    private static volatile boolean Valided;
    private static volatile boolean Opened;
    private static volatile boolean Connected;
    private static volatile List<BluetoothDevice> bondDevices;
    private static volatile List<BluetoothDevice> connectableDevices;
    private static volatile boolean Discoverying;//是否正在扫描


    static {
        Valided = false;
        Opened = false;
        Connected = false;
        Discoverying = false;
//        bondDevices = new ArrayList<BluetoothDevice>();
        connectableDevices = new ArrayList<BluetoothDevice>();
        bluetoothStatusData = new BluetoothStatusData();
    }



    public static boolean isDiscoverying() {
        return Discoverying;
    }

    public static void setDiscoverying(boolean isDiscoverying) {
        BluetoothStatusData.Discoverying = isDiscoverying;
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
        BluetoothStatusData.bondDevices = bondDevices;
    }
    public static void setBondDevices(Set<BluetoothDevice> connectableDevices) {
        List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
        devices.addAll(connectableDevices);
        BluetoothStatusData.connectableDevices = devices;
    }
//    public static void clearBondedDevice(){
//        bondDevices.clear();
//    }



    public static List<BluetoothDevice> getConnectableDevices() {
        return connectableDevices;
    }

    public static void setConnectableDevices(List<BluetoothDevice> connectableDevices) {
        BluetoothStatusData.connectableDevices = connectableDevices;
    }



    public static void addConnectedDevices(BluetoothDevice device){
        if(!connectableDevices.contains(device)){
            connectableDevices.add(device);
        }
    }

    public static void clearConnectedDevice(){
        connectableDevices.clear();
    }


    private BluetoothStatusData(){}

//  获取对象
    public static BluetoothStatusData getInstance(){
        return bluetoothStatusData;
    }
}
