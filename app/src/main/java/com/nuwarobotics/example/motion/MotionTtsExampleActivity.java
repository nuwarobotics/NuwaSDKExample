package com.nuwarobotics.example.motion;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nuwarobotics.example.R;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventListener;
import com.nuwarobotics.service.agent.VoiceEventListener;
import com.nuwarobotics.service.agent.VoiceResultJsonParser;

import java.util.ArrayList;

/**
 * Example of TTS and Motion, TODO: move to readMe later
 * ケビー：（おじぎのうごき）
 * ケビー：「こんにちは、僕はケビーです」
 * ケビー：「空が飛べたらいいのになあ」（羽ばたく動きをしながら話す）
 * ケビー：「無理だから、歩いていこう」
 * ケビー：「ぼく、動くのは得意なんですよ」（前に進みながら）
 */
public class MotionTtsExampleActivity extends AppCompatActivity {
    private final String TAG = "MotionTtsExampleActivity";
    NuwaRobotAPI mRobotAPI;
    IClientId mClientId;
    Button mStartDemoBtn ;

    Handler mHandler = new Handler();

    private int mCmdStep = 0 ;
    private boolean mTts_complete = true;
    private boolean mMotion_complete = true;

    //Following is Action pair config
    ArrayList<String> cmdTTS = new ArrayList<String>() {{
        add("");
        add("こんにちは、僕はケビーです");
        add("空が飛べたらいいのになあ");
        add("無理だから、歩いていこう");
        add("ぼく、動くのは得意なんですよ");
    }};//you can customize this list
    ArrayList<String> cmdMotion = new ArrayList<String>() {{
        add("666_RE_Hello");
        add("");
        add("666_IM_Bird");
        add("");
        add("666_SP_Walk");
    }};//you can customize this list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motiontts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TAG);

        mStartDemoBtn = (Button)findViewById(R.id.button);

        //Step 1 : Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this,mClientId);

        //Step 2 : Register receive Robot Event
        Log.d(TAG,"register EventListener ") ;
        mRobotAPI.registerRobotEventListener(robotEventListener);//listen callback of robot service event

        mStartDemoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick to start start demo") ;
                //Step 3 : reset command step and trigger action start thread
                mCmdStep = 0 ;
                mHandler.post(robotAction);//play next action
            }
        });

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

    Runnable robotAction = new Runnable() {
        @Override
        public void run() {
            String current_tts = cmdTTS.get(mCmdStep);
            String current_motion = cmdMotion.get(mCmdStep);
            Log.d(TAG,"Action Step "+mCmdStep+" TTS:"+current_tts+" motion:"+current_motion);
            //Config waiting flag first.   (Example : use to wait two callback ready)
            if(current_tts != "") mTts_complete = false;
            if(current_motion != "") mMotion_complete = false;

            //Start play tts and motion if need
            if(current_tts != "") mRobotAPI.startTTS(current_tts);

            //Please NOTICE that auto_fadein should assign false when motion file nothing to display
            if(current_motion != "") mRobotAPI.motionPlay(current_motion,false);


            while(mTts_complete == false  || mMotion_complete == false){
                //wait both action complete
            }

            //both TTS and Motion complete, we play next action
            mCmdStep ++ ;//next action step
            if(mCmdStep < cmdTTS.size()) {
                mHandler.post(robotAction);//play next action
            }else{
                mRobotAPI.motionReset();//Reset Robot pose to default
            }
        }
    };

    RobotEventListener robotEventListener = new RobotEventListener() {
        @Override
        public void onWikiServiceStart() {
            // Nuwa Robot SDK is ready now, you call call Nuwa SDK API now.
            Log.d(TAG,"onWikiServiceStart, robot ready to be control ") ;
            //Step 3 : Start Control Robot after Service ready.
            //Register Voice Callback event
            mRobotAPI.registerVoiceEventListener(voiceEventListener);//listen callback of robot voice related event
            //Allow user start demo after service ready
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Allow user click button.
                    mStartDemoBtn.setEnabled(true);//when service ready, we start allow user start API function call
                }
            });

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
            mMotion_complete = true;
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
    VoiceEventListener voiceEventListener = new VoiceEventListener() {
        @Override
        public void onWakeup(boolean b, String s, float v) {
            Log.d(TAG, "onWakeup:" + !b + ", score:" + s);

        }

        @Override
        public void onTTSComplete(boolean b) {
            Log.d(TAG, "onTTSComplete" + !b);
            mTts_complete = true;

        }

        @Override
        public void onSpeechRecognizeComplete(boolean b, ResultType resultType, String s) {

        }

        @Override
        public void onSpeech2TextComplete(boolean b, String s) {

        }

        @Override
        public void onMixUnderstandComplete(boolean b, ResultType resultType, String s) {
            Log.d(TAG, "onMixUnderstandComplete isError:" + !b + ", json:" + s);
            //Step 7 : Robot recognized the word of user speaking on  onMixUnderstandComplete
            //both startMixUnderstand and startLocalCommand will receive this callback
            String result_string = VoiceResultJsonParser.parseVoiceResult(s);
            Log.d(TAG,"onMixUnderstandComplete isError=" +b+" result_string="+result_string );
            //Step 8 : Request Robot speak what you want.
            switch(result_string){
                case "天気予報教えて":
                    mRobotAPI.startTTS("今日の天気は、晴れると思います");

                    break;
                case "おはよう":
                    mRobotAPI.startTTS("おはようございます");

                    break;
            }
        }

        @Override
        public void onSpeechState(ListenType listenType, SpeechState speechState) {

        }

        @Override
        public void onSpeakState(SpeakType speakType, SpeakState speakState) {

        }

        @Override
        public void onGrammarState(boolean b, String s) {
            Log.d(TAG,"onGrammarState isError=" +b+" info="+s );
            //Step 5 : Aallow user press button to trigger startLocalCommand after grammar setup ready
            //startLocalCommand only allow calling after Grammar Ready
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Allow user click button.
                    //mStartWakeupBtn.setEnabled(true);//when service ready, we start allow user start API function call
                }
            });


        }

        @Override
        public void onListenVolumeChanged(ListenType listenType, int i) {

        }

        @Override
        public void onHotwordChange(HotwordState hotwordState, HotwordType hotwordType, String s) {

        }
    };
}
