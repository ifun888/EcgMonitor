package com.liuzhuohui.bluetoothconnection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.liuzhuohui.base.Config;
import com.liuzhuohui.data.BluetoothData;
import com.liuzhuohui.data.BluetoothStatusData;
import com.yanzhenjie.permission.AndPermission;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BluetoothManagerImpl implements BluetoothManager {

    private static final String TAG = "Ecg-Monitor";
    
    
    public static Observable<BluetoothStatusData> btObserver;
    private static BluetoothAdapter bluetoothAdapter;
    private static Context mcontext;        //还没初始化的
    private static IntentFilter btFilter;
    private static BroadcastReceiver btBoardcastReceiver;
    private static IntentFilter btTransmitFilter;
    private static BroadcastReceiver btTransmitBoardcastReceiver;
    private static BluetoothSocket socket;


    static {
        Log.d(TAG,"进入BluetoothManagerImpl的静态初始化");
//        获取蓝牙适配器
        bluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
        if(null == bluetoothAdapter){
            BluetoothStatusData.setValided(false);
        }else {
            BluetoothStatusData.setValided(true);
        }
//        检查是否开启蓝牙
        BluetoothStatusData.setOpened(bluetoothAdapter.isEnabled());

    }

//    构造方法
    @Inject
    public BluetoothManagerImpl(@Named("context") Context context){
        Log.d(TAG,"开始类的初始化，传入context");
        mcontext = context;
        Log.d(TAG,"动态申请权限中");
        //        申请权限
        AndPermission.with(mcontext).runtime().permission(Config.bluetoothPermission).start();
    }



//    开启并扫描
    private static void openAndScan(){
        if(BluetoothStatusData.isValided()){
            if(!BluetoothStatusData.isOpened()){
                bluetoothAdapter.enable();
            }
            BluetoothStatusData.clearConnectedDevice();
            bluetoothAdapter.startDiscovery();
        }
    }

//    初始化广播
    private static void btStatusInit(){
        btFilter = new IntentFilter();
        btFilter.addAction(BluetoothDevice.ACTION_FOUND);
        btFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        btFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        btFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        btFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);

        btBoardcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    String action =intent.getAction();
                if (action == null)
                    return;
                switch (action){
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        Log.i(TAG,"开始扫描");
                        BluetoothStatusData.setDiscoverying(true);
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        Log.i(TAG,"完成扫描");
                        //完成扫描
                        BluetoothStatusData.setDiscoverying(false);

                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        //                        Log.i("Ecg-Home","发现设备");
                        BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Log.i(TAG,dev.getName()+" : "+dev.getAddress());
                        BluetoothStatusData.addConnectedDevices(dev);
                        break;
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,-1);
                        switch (state){
                            case BluetoothAdapter.STATE_ON:
                                Log.i(TAG,"蓝牙已打开");
                                BluetoothStatusData.setOpened(true);
                                break;
                            case BluetoothAdapter.STATE_OFF:
                                Log.i(TAG,"蓝牙已关闭");
                                BluetoothStatusData.setOpened(false);
                                break;
                            case BluetoothAdapter.STATE_CONNECTED:
                                Log.i(TAG,"蓝牙已连接");
                                BluetoothStatusData.setConnected(true);
                                break;
                            case BluetoothAdapter.STATE_DISCONNECTED:
                                Log.i(TAG,"蓝牙连接已断开");
                                BluetoothStatusData.setConnected(false);
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
                                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                                BluetoothStatusData.setBondDevices(devices);
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
    public  Observable<BluetoothData> connectByMac(String mac) {
        btStatusInit();
        openAndScan();

        BluetoothDevice device =bluetoothAdapter.getRemoteDevice(mac);
        if(null == device){
            return null;
        }
        bluetoothAdapter.cancelDiscovery();

//        需要申报异常
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString(Config.BLUETOOTH_UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Observable<BluetoothData> connectObservable = Observable
                .create(new ObservableOnSubscribe<BluetoothData>() {
                    @Override
                    public void subscribe(ObservableEmitter<BluetoothData> emitter) throws Exception {
                       try {
                           socket.connect();
                       }catch (IOException e){
                           Log.i(TAG,"socket连接失败！！！正在关闭Socket");
                           e.printStackTrace();

                           try {
                                socket.close();
                           }catch (IOException e1){
                               Log.i(TAG,"socket关闭失败！！！");
                               e1.printStackTrace();
                           }
                       }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        return null;
    }

    @Override
    public  Observable<BluetoothData> connectByName(String name) {

        return null;

    }

    @Override
    public void disconnectByMac(String mac) {

    }

    @Override
    public void disconnectByName(String name) {

    }


    @Override
    public Observable<BluetoothStatusData> getStatus(Observer observer) {
//        返回当前状态
        Observable<BluetoothStatusData> btObservable = Observable
                .just(BluetoothStatusData.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return btObservable;
    }




    @Override
    public int writeToBluetooth(byte[] writeBuffer) {
        return 0;
    }
}
