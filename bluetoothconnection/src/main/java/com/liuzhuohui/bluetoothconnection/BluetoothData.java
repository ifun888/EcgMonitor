package com.liuzhuohui.bluetoothconnection;

public class BluetoothData {
    public static volatile boolean connected;
    public static volatile byte[] readBuffer;
    public static volatile int index;

    static {
        index = 0;
        connected = false;
        readBuffer = new byte[10240];
    }
}
