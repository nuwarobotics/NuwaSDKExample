package com.nuwarobotics.example.activity;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.nuwarobotics.example.R;
import com.nuwarobotics.example.motion.base.BaseAppCompatActivity;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventListener;
import com.nuwarobotics.service.agent.VoiceEventListener;
import com.nuwarobotics.service.facecontrol.utils.ServiceConnectListener;

/**
 * Face Control API example 1
 * This example present
 * - How to initial and show robot face on Activity
 * - How to control mouth on off animation.
 * Target SDK : 2.1.0.02
 */
public class FaceControlExampleActivity extends BaseAppCompatActivity {
    private final static String TAG = "FaceControlExampleActivity";

    //nuwa general style of close button
    ImageButton mCloseBtn;
    Button mBtn_start ;

    NuwaRobotAPI mRobotAPI;
    IClientId mClientId;
    Context mContext ;

    private static final String TTS_SAMPLE = "Kebbi is speaking with face" ;
    private static final long FACE_MOUTH_SPEED = 200;//set larger value for slower mouth speed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facecontrol_show);
        mContext = this;

        mBtn_start = findViewById(R.id.btn_start);
        mBtn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Please make sure enable power key when leave ui
                showface(TTS_SAMPLE);
            }
        });

        mCloseBtn = findViewById(R.id.imgbtn_quit);
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Please make sure enable power key when leave ui
                if(mRobotAPI!=null){
                    mRobotAPI.enablePowerKey();
                }
                finish();
            }
        });
        //Step 1 : Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this,mClientId);



    }
    ServiceConnectListener FaceControlConnect = new ServiceConnectListener() {
        @Override
        public void onConnectChanged(ComponentName componentName, boolean b) {
            //isBindFace = b;
            Log.d(TAG, "faceService onbind : " + b);
        }
    };
    @Override
    protected int getLayoutRes(){
        return R.layout.activity_disablepowerkey;
    }

    @Override
    protected int getToolBarTitleRes(){
        return R.string.disablepowerkey_sdk_example_title;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume ") ;
        hideSystemUi();
        //Step 2 : Register receive Robot Event
        Log.d(TAG,"register EventListener ") ;
        mRobotAPI.registerRobotEventListener(robotEventListener);//listen callback of robot service event

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause ") ;
        //Please make sure enable power key when leave ui
        if(mRobotAPI!=null){
            mRobotAPI.UnityFaceManager().release();
            mRobotAPI.release();
        }
    }

    @Override
    protected void onDestroy() {
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
    RobotEventListener robotEventListener = new RobotEventListener() {
        @Override
        public void onWikiServiceStart() {
            // Nuwa Robot SDK is ready now, you call call Nuwa SDK API now.
            Log.d(TAG,"onWikiServiceStart, robot ready to be control ") ;
            //Step 3 : Start Control Robot after Service ready.
            //Register Voice Callback event
            mRobotAPI.registerVoiceEventListener(voiceEventListener);//listen callback of robot voice related event

            Log.d(TAG,"initial Face Control");
            //mRobotAPI.disablePowerKey();
            mRobotAPI.initFaceControl(mContext, mContext.getClass().getName(), FaceControlConnect);
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


    private void showface(String tts) {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().showUnity();//lunch face
            mRobotAPI.startTTS(tts);//speak a TTS
            mouthOn(FACE_MOUTH_SPEED);//starting mouth on animation
        } else {
            Log.d(TAG, " === mNuwaRobotAPI null ===  please init");
        }
    }

    private void hideface() {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().hideUnity();//hide face
        } else {
            Log.d(TAG, " === mNuwaRobotAPI null ===  please init");
        }
    }

    private void mouthOn(long speed) {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().mouthOn(speed);
        } else {
            Log.d(TAG, " === mNuwaRobotAPI null ===  please init");
        }
    }

    private void mouthOff() {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().mouthOff();
        } else {
            Log.d(TAG, " === mNuwaRobotAPI null ===  please init");
        }
    }


    VoiceEventListener voiceEventListener = new VoiceEventListener() {
        @Override
        public void onWakeup(boolean isError, String score, float direction) {

        }

        @Override
        public void onTTSComplete(boolean isError) {
            Log.d(TAG, "onTTSComplete:" + !isError);
            //you could postDelay a timing to hide face for better user experience
            mouthOff();
            hideface();

        }

        @Override
        public void onSpeechRecognizeComplete(boolean isError, ResultType iFlyResult, String json) {

        }

        @Override
        public void onSpeech2TextComplete(boolean isError, String json) {

        }

        @Override
        public void onMixUnderstandComplete(boolean isError, ResultType resultType, String s) {

        }

        @Override
        public void onSpeechState(ListenType listenType, SpeechState speechState) {

        }

        @Override
        public void onSpeakState(SpeakType speakType, SpeakState speakState) {
            Log.d(TAG, "onSpeakState:" + speakType + ", state:" + speakState);
        }

        @Override
        public void onGrammarState(boolean isError, String s) {

        }

        @Override
        public void onListenVolumeChanged(ListenType listenType, int i) {

        }

        @Override
        public void onHotwordChange(HotwordState hotwordState, HotwordType hotwordType, String s) {

        }
    };
}
