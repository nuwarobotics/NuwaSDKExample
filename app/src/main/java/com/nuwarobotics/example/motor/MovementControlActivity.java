package com.nuwarobotics.example.motor;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.nuwarobotics.example.R;
import com.nuwarobotics.example.util.Logger;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MovementControlActivity extends AppCompatActivity {
    NuwaRobotAPI mRobotAPI;
    IClientId mClientId;

    ListView mMovementListLow;
    ListView mMovementListAdvance;
    ArrayAdapter<String> mMovementListLowAdapter;
    ArrayAdapter<String> mMovementListAdvanceAdapter;
    ArrayList<String> listLow;
    ArrayList<String> listAdvance;
    private Map<String, Moveable> lowLevelControlMap= new HashMap<>();
    private Map<String, Moveable> advanceControlMap= new HashMap<>();

    private Switch mWheelSwitch;
    private CheckBox mDropDectionCheckBox;

    private HandlerThread handlerThread;
    private MessageHandler mMessageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        //Step 1 : Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this, mClientId);

        //Step 2 : Register to receive Robot Event
        Logger.d("register RobotEventCallback ") ;
        mRobotAPI.registerRobotEventListener(mRobotEventCallback);//listen callback of robot service event
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
        mRobotAPI.release();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMovementHandler(){
        handlerThread = new HandlerThread("Message Handler");
        handlerThread.start();
        mMessageHandler = new MessageHandler(handlerThread.getLooper());
    }

    private void initMovementCb(){
        runOnUiThread(()->{
            //init low level control
            lowLevelControlMap.put("move(float val) max: 0.2(Meter/sec) go forward, min: -0.2(Meter/sec) go back", ()->mRobotAPI.move(new Random().nextFloat() * 0.4f - 0.2f)); //-0.2f ~ 0.2f
            lowLevelControlMap.put("turn(float val) max: 30(Degree/sec) turn left, min: -30(Degree/sec) turn right", ()->mRobotAPI.turn(new Random().nextInt(30 - (-30) ) - 30)); //-30 ~ 30 degree

            listLow.clear();
            listLow.addAll(lowLevelControlMap.keySet());

            advanceControlMap.put("forwardInAccelerationEx()", mRobotAPI::forwardInAccelerationEx);
            advanceControlMap.put("backInAccelerationEx()", mRobotAPI::backInAccelerationEx);
            advanceControlMap.put("stopInAccelerationEx()", mRobotAPI::stopInAccelerationEx);
            advanceControlMap.put("turnLeftEx()", mRobotAPI::turnLeftEx);
            advanceControlMap.put("turnRightEx()", mRobotAPI::turnRightEx);
            advanceControlMap.put("stopTurnEx()", mRobotAPI::stopTurnEx);

            listAdvance.clear();
            listAdvance.addAll(advanceControlMap.keySet());

            //update list view
            mMovementListLowAdapter.notifyDataSetChanged();
            mMovementListAdvanceAdapter.notifyDataSetChanged();
        });
    }

    private String getWheelSwitchText(boolean lock_unlock){
        String sLocked = getResources().getString(R.string.movement_text_lock);
        String sUnlocked = getResources().getString(R.string.movement_text_unlock);
        if(lock_unlock)
            return getResources().getString(R.string.movement_text_wheel, sLocked);
        else
            return getResources().getString(R.string.movement_text_wheel, sUnlocked);
    }

    private void initSwitchState(){
        runOnUiThread(()->{
            mWheelSwitch.setChecked(false);
            mWheelSwitch.setText(getWheelSwitchText(false));
            mWheelSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
                mWheelSwitch.setEnabled(false);
                if(isChecked)
                    mRobotAPI.lockWheel();
                else
                    mRobotAPI.unlockWheel();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    //do nothing
                }
                mWheelSwitch.setText(getWheelSwitchText(isChecked));
                mWheelSwitch.setEnabled(true);
            });

            mDropDectionCheckBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
                if(isChecked)
                    mRobotAPI.requestSensor(NuwaRobotAPI.SENSOR_DROP);
                else
                    mRobotAPI.stopSensor(NuwaRobotAPI.SENSOR_DROP);
            });
        });
    }

    private void initView(){
        setContentView(R.layout.activity_movementcontrol);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(this.getClass().getCanonicalName());
        }

        //init Switch
        mWheelSwitch = findViewById(R.id.lock_wheel);

        //init CheckBox
        mDropDectionCheckBox = findViewById(R.id.detect_drop);

        //init list view
        listLow = new ArrayList<>(lowLevelControlMap.keySet());
        mMovementListLowAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listLow);
        mMovementListLow = findViewById(R.id.movement_list_low);
        mMovementListLow.setAdapter(mMovementListLowAdapter);
        mMovementListLow.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) ->{
                Logger.d( "mMovementListLow: " + mMovementListLowAdapter.getItem(position) + " is clicked!");
                if(mMessageHandler != null){
                    mMessageHandler.obtainMessage(MessageHandler.MSG_START_MOVING, mMovementListLowAdapter.getItem(position)).sendToTarget();
                    //stop moving after 1s
                    mMessageHandler.sendMessageDelayed(mMessageHandler.obtainMessage(MessageHandler.MSG_STOP_MOVING), 1000);
                } else
                    Toast.makeText(getApplicationContext(), "Not Ready to Moving, Click after a while", Toast.LENGTH_SHORT).show();
        });

        listAdvance = new ArrayList<>(advanceControlMap.keySet());
        mMovementListAdvanceAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listAdvance);
        mMovementListAdvance = findViewById(R.id.movement_list_advance);
        mMovementListAdvance.setAdapter(mMovementListAdvanceAdapter);
        mMovementListAdvance.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) ->{
                Logger.d( "mMovementListAdvance: " + mMovementListAdvanceAdapter.getItem(position) + " is clicked!");
                if(mMessageHandler != null){
                    mMessageHandler.obtainMessage(MessageHandler.MSG_START_MOVING, mMovementListAdvanceAdapter.getItem(position)).sendToTarget();
                    //stop moving after 1s
                    mMessageHandler.sendMessageDelayed(mMessageHandler.obtainMessage(MessageHandler.MSG_STOP_MOVING), 1000);
                } else
                    Toast.makeText(getApplicationContext(), "Not Ready to Moving, Click after a while", Toast.LENGTH_SHORT).show();
        });
    }

    private RobotEventCallback mRobotEventCallback = new RobotEventCallback() {
        @Override
        public void onWindowSurfaceReady() {
            Logger.d("mRobotEventCallback.onWindowSurfaceReady()");
        }

        @Override
        public void onWikiServiceStop() {
            Logger.d("onWikiServiceStop");

        }

        @Override
        public void onWikiServiceStart() {
            Logger.d("onWikiServiceStart");
            //ready to moving
            initMovementHandler();
            initMovementCb();
            initSwitchState();
        }

        @Override
        public void onWikiServiceCrash() {
            Logger.d("onWikiServiceCrash");

        }

        @Override
        public void onWikiServiceRecovery() {
            Logger.d("onWikiServiceRecovery");

        }

        @Override
        public void onDropSensorEvent(int value) {
            Toast.makeText(getApplicationContext(), "onDropSensorEvent(" + value + ") received", Toast.LENGTH_SHORT).show();
        }
    };


    private class MessageHandler extends Handler {
        private static final int MSG_START_MOVING = 1;
        private static final int MSG_STOP_MOVING = 2;

        MessageHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Logger.d("handleMessage(" + msg + ")");
            switch (msg.what){
                case MSG_START_MOVING:
                    String key = (String)msg.obj;
                    Moveable moveable = null;
                    if(lowLevelControlMap != null && lowLevelControlMap.containsKey(key))
                        moveable = lowLevelControlMap.get(key);
                    else if(advanceControlMap !=null && advanceControlMap.containsKey(key))
                        moveable = advanceControlMap.get(key);

                    if(moveable != null)
                        moveable.moveCb();
                    else
                        Logger.d("Cannot find related moveable callback to execute for \"" + key + "\"");
                    break;
                case MSG_STOP_MOVING:
                    mRobotAPI.turn(0);
                    mRobotAPI.move(0);
                    mRobotAPI.stopInAccelerationEx();
                    mRobotAPI.stopTurnEx();
                    break;
            }
        }
    }

    interface Moveable{
        void moveCb();
    }
}
