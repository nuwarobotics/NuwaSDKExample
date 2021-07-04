package com.nuwarobotics.example.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nuwarobotics.example.R;
import com.nuwarobotics.example.motion.base.BaseAppCompatActivity;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventListener;
import com.nuwarobotics.service.agent.VoiceEventListener;
import com.nuwarobotics.service.facecontrol.utils.ServiceConnectListener;

import com.nuwarobotics.app.nuwableinterface.BLEConstant;
import com.nuwarobotics.app.nuwableinterface.INuwaBLEInterface;

/**
 * Ble connection example for NH1
 * This example present
 * - How to start video call
 * Target SDK : 2.1.0.02
 */
public class StartBLEConnnection extends BaseAppCompatActivity {
    private final static String TAG = "StartBLEConnnection";

    //nuwa general style of close button
    ImageButton mCloseBtn;
    Button mBtnDemo;
    Context mContext;
    INuwaBLEInterface mNuwaBLEService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_measure);
        mContext = this;

        mBtnDemo = findViewById(R.id.btn_start);
        mBtnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Query ble status, if NH1 is not connected, launch ble connection setup page
                try {
                    if (mNuwaBLEService.getBLEStatus(BLEConstant.BLE_MODE_NUWA_THERMOMETER_VSS) != BLEConstant.STATE_CONNECTED) {
                        Intent intent = new Intent("com.nuwarobotics.health.action.ble_connect");
                        startActivityForResult(intent, 0);
                    }
                }
                catch (RemoteException e){
                    Log.e(TAG, "Get Ble status error:" + e);
                }
            }
        });

        mCloseBtn = findViewById(R.id.imgbtn_quit);
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume ") ;
        hideSystemUi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause ") ;
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
        Log.d(TAG,"onDestroy ") ;
    }

    protected void hideSystemUi() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // Just initial BLEService start(trigger service initial)
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mNuwaBLEService = INuwaBLEInterface.Stub.asInterface(service);
            Log.e(TAG, "onServiceConnected");
            try {
                if (mNuwaBLEService.isBluetoothReady()) {
                    // Automatically connects to the device upon successful start-up initialization.

                    if (mNuwaBLEService.getBLEStatus(BLEConstant.BLE_MODE_NUWA_THERMOMETER_VSS) == BLEConstant.STATE_CONNECTED) {
                        //BLE device is connected
                    } else {
                        //BLE device is not connnected
                    }
                }
            }catch (RemoteException e) {
                Log.e(TAG, "exception "+ e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mNuwaBLEService = null;
            Log.e(TAG,"onServiceDisconnected");
        }
    };

    protected void onStart() {
        super.onStart();
        bindService(new Intent(HealthConstant.NUWA_BLE_SERVICE_ACTION).setPackage(HealthConstant.NUWA_BLE_SERVICE_PACKAGE)
                    , mServiceConnection, BIND_AUTO_CREATE);
    }
}
