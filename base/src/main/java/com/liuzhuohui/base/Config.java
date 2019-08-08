package com.liuzhuohui.base;

import android.Manifest;

public class Config {

    public static final String[] bluetoothPermission = {
            //权限列表
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,//6.0以上权限
            Manifest.permission.ACCESS_COARSE_LOCATION,//6.0以上权限
    };


    public static final String BLUETOOTH_UUID = "00001101-0000-1000-8000-00805F9B34FB";
}
