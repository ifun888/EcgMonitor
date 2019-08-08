package com.liuzhuohui.bluetoothconnection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BlendMode;
import android.util.Log;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BluetoothManagerImpl implements BluetoothManager {

    private static final String TAG = "Ecg-Home";
    
    
    public static Observable<BluetoothData> btObserver;
    private static BluetoothAdapter bluetoothAdapter;
    private static Context mcontext;        //还没初始化的
    private static IntentFilter btFilter;
    private static BroadcastReceiver btBoardcastReceiver;



    static {
//        获取蓝牙适配器
        bluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
        if(null == bluetoothAdapter){
            BluetoothData.setValided(false);
        }else {
            BluetoothData.setValided(true);
        }
//        检查是否开启蓝牙
        BluetoothData.setOpened(bluetoothAdapter.isEnabled());
    }

//    构造方法
    @Inject
    public BluetoothManagerImpl(@Named("context") Context context){
        mcontext = context;
    }



//    开启并扫描
    private static void openAndScan(){
        if(BluetoothData.isValided()){
            if(!BluetoothData.isOpened()){
                bluetoothAdapter.enable();
            }
            bluetoothAdapter.startDiscovery();
        }
    }

//    初始化广播
    private static void initBoardcast(){
        btFilter = new IntentFilter();
        btFilter.addAction(BluetoothDevice.ACTION_FOUND);
        btFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        btFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        btFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        btFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);

        btBoardcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    String action =intent.getAction();
                if (action == null)
                    return;
                switch (action){
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        Log.i("Ecg-Home","开始扫描");
                        BluetoothData.setDiscoverying(true);
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        Log.i("Ecg-Home","完成扫描");
                        //完成扫描
                        BluetoothData.setDiscoverying(false);

                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        //                        Log.i("Ecg-Home","发现设备");
                        BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Log.i("11111",dev.getName()+" : "+dev.getAddress());
//                        if(!btConnectableDevices.contains(dev))
//                            btConnectableDevices.add(dev);
                        break;
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,-1);
                        switch (state){
                            case BluetoothAdapter.STATE_ON:
                                Log.i(TAG,"蓝牙已打开");
                                BluetoothData.setOpened(true);
                                break;
                            case BluetoothAdapter.STATE_OFF:
                                Log.i(TAG,"蓝牙已关闭");
                                BluetoothData.setOpened(false);
                                break;
                            case BluetoothAdapter.STATE_CONNECTED:
                                Log.i(TAG,"蓝牙已连接");
                                BluetoothData.setConnected(true);
                                break;
                            case BluetoothAdapter.STATE_DISCONNECTED:
                                Log.i(TAG,"蓝牙连接已断开");
                                BluetoothData.setConnected(false);
                                break;
                            default:
                                break;
                        }
                        break;
                    case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                        BluetoothDevice pinDev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        switch (pinDev.getBondState()){
                            case BluetoothDevice.BOND_BONDED:
                                Log.i(TAG,"配对成功");
//                                BluetoothData.getBondDevices().clear();
//                                BluetoothData.getBondDevices().addAll(bluetoothAdapter.getBondedDevices());
//                                Set<BluetoothDevice> bondDev = bluetoothAdapter.getBondedDevices();
//                                btBondDevices.addAll(bondDev);
//                                BluetoothData.setBondDevices(btBondDevices);

                                break;
                            case  BluetoothDevice.BOND_BONDING:
                                Log.i(TAG,"配对中");
                                break;
                            case BluetoothDevice.BOND_NONE:
                                Log.i(TAG,"取消配对");
                                break;
                            default:
                                break;
                        }
                    default:
                        break;
                }
            }
        };

//      对接广播
        mcontext.registerReceiver(btBoardcastReceiver,btFilter);
    }

    @Override
    public void connectByMac(String mac) {

    }

    @Override
    public void connectByName(String name) {

    }

    @Override
    public void disconnectByMac(String mac) {

    }

    @Override
    public void disconnectByName(String name) {

    }


    @Override
    public Observable<BluetoothData> getStatus(Observer observer) {
//        返回当前状态
        Observable<BluetoothData> btObservable = Observable
                .just(BluetoothData.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return btObservable;
    }
}
