package com.nuwarobotics.example.led;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import nuwa sdk
import com.nuwarobotics.example.R;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventListener;

public class LEDExampleActivity extends AppCompatActivity {
    private final String TAG = "NuwaSDKExampleLED";
    NuwaRobotAPI mRobotAPI;
    IClientId mClientId;
    Button mBTN_DisableSystemLED, mBTN_EnableSystemLED;
    Button mBTN_FACE,mBTN_BODY,mBTN_HAND_LEFT,mBTN_HAND_RIGHT;
    EditText mEditR,mEditG,mEditB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_example_led);

        mBTN_DisableSystemLED = (Button)findViewById(R.id.btn_DisableSystemLED6);
        mBTN_DisableSystemLED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick to Disable System LED") ;
                //Disable System LED
                mRobotAPI.disableSystemLED();
            }
        });
        mBTN_EnableSystemLED = (Button)findViewById(R.id.btn_EnableSystemLED);
        mBTN_EnableSystemLED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick to Enable System LED") ;
                //Enable System LED
                mRobotAPI.enableSystemLED();
            }
        });

        mBTN_FACE = (Button)findViewById(R.id.btn_face);
        mBTN_BODY = (Button)findViewById(R.id.btn_body);
        mBTN_HAND_LEFT = (Button)findViewById(R.id.btn_hand_left);
        mBTN_HAND_RIGHT = (Button)findViewById(R.id.btn_hand_right);

        mEditR = (EditText) findViewById(R.id.editTextR);
        mEditG = (EditText) findViewById(R.id.editTextG);
        mEditB = (EditText) findViewById(R.id.editTextB);

        //Step 1 : Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this,mClientId);

        //Step 2 : Register receive Robot Event
        Log.d(TAG,"register EventListener ") ;
        mRobotAPI.registerRobotEventListener(robotEventListener);//listen callback of robot service event

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // release Nuwa Robot SDK resource
        mRobotAPI.release();
    }

    public void onLEDClick(View view){
        /**
                *API:setLedColor
                * Set LED color to target LED.
                * Please disable system control LED by {@link #disableSystemLED()} before you use setLedColor
                *
                * @param id id of LED requested; LED_ID_HEAD = 1, LED_ID_CHEST =  2, LED_ID_RIGHT_HAND = 3, LED_ID_LEFT_HAND = 4
                *    1 = two side of face LED (臉兩側LED燈)
                *    2 = neck(or chest) LED (胸前LED燈)
                *    3 = Left hand LED (左手LED燈)
                *    4 = Right hand LED (右手LED燈)
                * @param brightness The brightness value from 0 to 255.
                * @param r The RED color value from 0 to 255.
                * @param g The GREEN color value from 0 to 255.
                * @param b The BLUE color value from 0 to 255.
                */
        switch(view.getId()) {
            case R.id.btn_face :
                mRobotAPI.setLedColor(1,255,Integer.parseInt(mEditR.getText().toString()),Integer.parseInt(mEditG.getText().toString()),Integer.parseInt(mEditB.getText().toString()));
                break;
            case R.id.btn_body :
                mRobotAPI.setLedColor(2,255,Integer.parseInt(mEditR.getText().toString()),Integer.parseInt(mEditG.getText().toString()),Integer.parseInt(mEditB.getText().toString()));
                break;
            case R.id.btn_hand_left :
                mRobotAPI.setLedColor(3,255,Integer.parseInt(mEditR.getText().toString()),Integer.parseInt(mEditG.getText().toString()),Integer.parseInt(mEditB.getText().toString()));
                break;
            case R.id.btn_hand_right :
                mRobotAPI.setLedColor(4,255,Integer.parseInt(mEditR.getText().toString()),Integer.parseInt(mEditG.getText().toString()),Integer.parseInt(mEditB.getText().toString()));
                break;
        }

    }

    RobotEventListener robotEventListener = new RobotEventListener() {
        @Override
        public void onWikiServiceStart() {
            // Nuwa Robot SDK is ready now, you call call Nuwa SDK API now.
            Log.d(TAG,"onWikiServiceStart, robot ready to be control ") ;
        }

        @Override
        public void onWikiServiceStop() {

        }

        @Override
        public void onWikiServiceCrash() {

        }

        @Override
        public void onWikiServiceRecovery() {

        }

        @Override
        public void onStartOfMotionPlay(String s) {

        }

        @Override
        public void onPauseOfMotionPlay(String s) {

        }

        @Override
        public void onStopOfMotionPlay(String s) {

        }

        @Override
        public void onCompleteOfMotionPlay(String s) {
            Log.d(TAG,"Play Motion Complete " + s);
        }

        @Override
        public void onPlayBackOfMotionPlay(String s) {

        }

        @Override
        public void onErrorOfMotionPlay(int i) {

        }

        @Override
        public void onPrepareMotion(boolean b, String s, float v) {

        }

        @Override
        public void onCameraOfMotionPlay(String s) {

        }

        @Override
        public void onGetCameraPose(float v, float v1, float v2, float v3, float v4, float v5, float v6, float v7, float v8, float v9, float v10, float v11) {

        }

        @Override
        public void onTouchEvent(int i, int i1) {

        }

        @Override
        public void onPIREvent(int i) {

        }

        @Override
        public void onTap(int i) {

        }

        @Override
        public void onLongPress(int i) {

        }

        @Override
        public void onWindowSurfaceReady() {

        }

        @Override
        public void onWindowSurfaceDestroy() {

        }

        @Override
        public void onTouchEyes(int i, int i1) {

        }

        @Override
        public void onRawTouch(int i, int i1, int i2) {

        }

        @Override
        public void onFaceSpeaker(float v) {

        }

        @Override
        public void onActionEvent(int i, int i1) {

        }

        @Override
        public void onDropSensorEvent(int i) {

        }

        @Override
        public void onMotorErrorEvent(int i, int i1) {

        }
    };
}
