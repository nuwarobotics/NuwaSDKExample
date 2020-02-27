package com.nuwarobotics.example.motor;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nuwarobotics.example.R;
import com.nuwarobotics.example.util.Logger;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MotorControlActivity extends AppCompatActivity {
    NuwaRobotAPI mRobotAPI;
    IClientId mClientId;
    ListView mMotorListView;

    private HandlerThread handlerThread;
    private MessageHandler handler;

    enum Motor{
        neck_y(1),
        neck_z(2),
        right_shoulder_z(3),
        right_shoulder_y(4),
        right_shoulder_x(5),
        right_elbow_y(6),
        left_shoulder_z(7),
        left_shoulder_y(8),
        left_shoulder_x(9),
        left_elbow_y(10);

        private int motorId;
        private static SparseArray<Motor> array = new SparseArray<>();

        static {
            for(Motor motor : Motor.values())
                array.put(motor.motorId, motor);
        }

        Motor(int motorId) {
            this.motorId = motorId;
        }

        public int getMotorId() {
            return this.motorId;
        }

        public static Motor valueOf(int motorId){
            return array.get(motorId);
        }

        public static String[] getStringArray(){
            return Arrays.toString(Motor.values()).replaceAll("^.|.$", "").split(", ");
        }
    }

    private static Map<Motor, Integer> moveRecordMap = new HashMap<>();

    static {
        for(Motor motor : Motor.values())
            moveRecordMap.put(motor, 20); //default is 20 degree
    }

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
        mRobotAPI.release();
        handlerThread.quit();
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

    private void initMotionHandler(){
        handlerThread = new HandlerThread("Motion Handler");
        handlerThread.start();

        handler = new MessageHandler(handlerThread.getLooper());
    }

    private void initView(){
        setContentView(R.layout.activity_motorcontrol);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(this.getClass().getCanonicalName());
        }

        //init list view
        final ArrayList<String> listArray = new ArrayList<>(Arrays.asList(Motor.getStringArray()));
        mMotorListView = findViewById(R.id.motion_list);
        mMotorListView.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listArray));
        mMotorListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.d( "mMotorListView: " + listArray.get(position) + " is clicked!");
                if(handler != null){
                    handler.obtainMessage(MessageHandler.MSG_START_MOTOR_MOVING, position + 1/*motorId start from 1*/, 0).sendToTarget();
                } else
                    Toast.makeText(getApplicationContext(), "Not Ready to Moving, Click after a while", Toast.LENGTH_SHORT).show();
            }
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
            initMotionHandler();
        }

        @Override
        public void onWikiServiceCrash() {
            Logger.d("onWikiServiceCrash");

        }

        @Override
        public void onWikiServiceRecovery() {
            Logger.d("onWikiServiceRecovery");

        }
    };

    private void moveMotor(int motorId){
        if(moveRecordMap.containsKey(Motor.valueOf(motorId))){
            Integer current_degree = moveRecordMap.get(Motor.valueOf(motorId));
            if(current_degree != null) {
                mRobotAPI.ctlMotor(motorId, 0, current_degree, 45); // default SpeedInDegreePerSec is 45
                moveRecordMap.put(Motor.valueOf(motorId), (-1) * current_degree); // reverse the degree
            } else
                Logger.d("No related degree parameter for " + Motor.valueOf(motorId));
        } else
            Logger.d("No related motor config for " + Motor.valueOf(motorId));

    }

    private class MessageHandler extends Handler {
        private static final int MSG_START_MOTOR_MOVING = 1;
        private static final int MSG_STOP_MOTOR_MOVING = 2;

        public MessageHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Logger.d("handleMessage(" + msg + ")");
            switch (msg.what){
                case MSG_START_MOTOR_MOVING:
                    moveMotor(msg.arg1);
                    break;
                case MSG_STOP_MOTOR_MOVING:
                    break;
            }
        }
    }
}
