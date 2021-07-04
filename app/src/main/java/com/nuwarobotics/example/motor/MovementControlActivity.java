package com.nuwarobotics.example.motor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.nuwarobotics.example.R;
import com.nuwarobotics.example.util.Logger;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventCallback;

public class MovementControlActivity extends AppCompatActivity {
    NuwaRobotAPI mRobotAPI;
    IClientId mClientId;

    private Switch mWheelSwitch;
    private CheckBox mDropDetectionCheckBox;
    private EditText mEditTextForwardBackward;
    private EditText mEditTextForwardBackwardKeep;
    private EditText mEditTextTurn;
    private EditText mEditTextTurnKeep;

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

            mDropDetectionCheckBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
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
        mDropDetectionCheckBox = findViewById(R.id.detect_drop);

        //init EditText
        mEditTextForwardBackward = findViewById(R.id.edit_movement_forward_backward);
        mEditTextForwardBackwardKeep = findViewById(R.id.edit_movement_forward_backward_seconds);
        mEditTextTurn = findViewById(R.id.edit_movement_turn);
        mEditTextTurnKeep = findViewById(R.id.edit_movement_turn_seconds);

        //init Button action
        findViewById(R.id.button_movement_go).setOnClickListener(v -> {
            try {
                float value = Float.parseFloat(mEditTextForwardBackward.getText().toString());
                if (0 != value) {
                    mRobotAPI.move(value);
                    int second = Integer.parseInt(mEditTextForwardBackwardKeep.getText().toString()) * 1000;
                    if (0 < second) {
                        mEditTextForwardBackwardKeep.postDelayed(() -> mRobotAPI.move(0), second);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                float value = Float.parseFloat(mEditTextTurn.getText().toString());
                if (0 != value) {
                    mRobotAPI.turn(value);
                    int second = Integer.parseInt(mEditTextTurnKeep.getText().toString()) * 1000;
                    if (0 < second) {
                        mEditTextTurnKeep.postDelayed(() -> mRobotAPI.turn(0), second);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        findViewById(R.id.button_movement_stop).setOnClickListener(v -> {
            mRobotAPI.move(0);
            mRobotAPI.turn(0);
        });
    }

    private final RobotEventCallback mRobotEventCallback = new RobotEventCallback() {
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
}
