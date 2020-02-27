package com.nuwarobotics.example.voice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nuwarobotics.example.R;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventListener;
import com.nuwarobotics.service.agent.SimpleGrammarData;
import com.nuwarobotics.service.agent.VoiceEventListener;
import com.nuwarobotics.service.agent.VoiceResultJsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocalcmdActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    NuwaRobotAPI mRobotAPI;
    IClientId mClientId;

    boolean mSDKinit = false;
    EditText mResult;
    Button mStartBtn;
    Button mStopBtn;

    //prepare local command list
    ArrayList<String> cmdList = new ArrayList<String>() {{
        add("今日の天気");
        add("おはよう");
    }};//you can customize this list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TAG);

        mResult = (EditText) findViewById(R.id.text_result);
        mResult.setText("");
        mStartBtn = (Button) findViewById(R.id.btn_start);
        mStartBtn.setEnabled(false);
        mStopBtn = (Button) findViewById(R.id.btn_stop);
        mStopBtn.setEnabled(false);

        //Step 1 : Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this, mClientId);

        //Step 2 : Register receive Robot Event
        Log.d(TAG, "register EventListener ");
        mRobotAPI.registerRobotEventListener(robotEventListener);//listen callback of robot service event

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // release Nuwa Robot SDK resource
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

    public void BtnStart(View view) {
        if (!mSDKinit) {
            setText("need to do SDK init first !!!", false);
            return;
        }

        setText(getCurrentTime() + "Start Localcmd, You can say: ", true);
        for (String cmd : cmdList)
            setText(cmd + ", ", true);
        setText("", false);

        //Step 6 : call start listen by local command which registered by createGrammar
        Log.d(TAG, "onClick to start startLocalCommand");
        //when Robot listen string you setup by createGrammar
        mRobotAPI.startLocalCommand();//Start listen without wakeup, callback on onMixUnderstandComplete

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartBtn.setEnabled(false);
                mStopBtn.setEnabled(true);
            }
        });
    }

    public void BtnStop(View view) {
        setText(getCurrentTime() + "Stop Localcmd", false);
        mRobotAPI.stopListen();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartBtn.setEnabled(true);
                mStopBtn.setEnabled(false);
            }
        });
    }

    private void setText(final String text, final boolean append) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResult.append(text);
                if (!append)
                    mResult.append("\n");
                mResult.setMovementMethod(ScrollingMovementMethod.getInstance());
                mResult.setSelection(mResult.getText().length(), mResult.getText().length());
            }
        });
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH:mm:ss ");
        String currentDateAndTime = sdf.format(new Date());
        return currentDateAndTime;
    }

    void prepareGrammarToRobot() {
        Log.d(TAG, "prepareGrammarToRobot ");

        //Create Grammar class object
        //NOTICE : please only use "lower case letter" as naming of grammar name
        SimpleGrammarData mGrammarData = new SimpleGrammarData("example");
        //setup local command list to grammar class
        for (String string : cmdList) {
            mGrammarData.addSlot(string);
            Log.d(TAG, "add string : " + string);
        }
        //generate grammar data
        mGrammarData.updateBody();
        //create and update Grammar to Robot
        Log.d(TAG, "createGrammar " + mGrammarData.body);
        //NOTICE : please only use "lower case letter" as naming of grammar name
        mRobotAPI.createGrammar(mGrammarData.grammar, mGrammarData.body); // Regist cmd
    }

    RobotEventListener robotEventListener = new RobotEventListener() {
        @Override
        public void onWikiServiceStart() {
            // Nuwa Robot SDK is ready now, you call call Nuwa SDK API now.
            Log.d(TAG, "onWikiServiceStart, robot ready to be control");
            //Step 3 : Start Control Robot after Service ready.
            //Register Voice Callback event
            mRobotAPI.registerVoiceEventListener(voiceEventListener);//listen callback of robot voice related event
            //Allow user start demo after service ready
            //TODO
            setText(getCurrentTime() + "onWikiServiceStart, robot ready to be control", false);
            mSDKinit = true;

            //Step 4 : prepare local command grammar
            prepareGrammarToRobot();//prepare local command grammar after service ready
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
    VoiceEventListener voiceEventListener = new VoiceEventListener() {
        @Override
        public void onWakeup(boolean isError, String score, float direction) {

        }

        @Override
        public void onTTSComplete(boolean isError) {

        }

        @Override
        public void onSpeechRecognizeComplete(boolean isError, ResultType iFlyResult, String json) {

        }

        @Override
        public void onSpeech2TextComplete(boolean isError, String json) {
            Log.d(TAG, "onSpeech2TextComplete:" + !isError + ", json:" + json);
        }

        @Override
        public void onMixUnderstandComplete(boolean isError, ResultType resultType, String s) {
            Log.d(TAG, "onMixUnderstandComplete isError:" + !isError + ", json:" + s);
            //Step 7 : Robot recognized the word of user speaking on  onMixUnderstandComplete
            //both startMixUnderstand and startLocalCommand will receive this callback
            String result_string = VoiceResultJsonParser.parseVoiceResult(s);
            //Step 8 : Request Robot speak what you want.
            setText(getCurrentTime() + "onMixUnderstandComplete:" + !isError + ", result:" + result_string, false);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Allow user click button.
                    mStartBtn.setEnabled(true);
                    mStopBtn.setEnabled(false);
                }
            });
        }

        @Override
        public void onSpeechState(ListenType listenType, SpeechState speechState) {

        }

        @Override
        public void onSpeakState(SpeakType speakType, SpeakState speakState) {

        }

        @Override
        public void onGrammarState(boolean isError, String s) {
            //Step 5 : Aallow user press button to trigger startLocalCommand after grammar setup ready
            //startLocalCommand only allow calling after Grammar Ready
            if (!isError) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStartBtn.setEnabled(true);
                        mStopBtn.setEnabled(false);
                    }
                });
            } else {
                Log.d(TAG, "onGrammarState error, " + s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Allow user click button.
                        mStartBtn.setEnabled(true);
                        mStopBtn.setEnabled(false);
                    }
                });
            }
        }

        @Override
        public void onListenVolumeChanged(ListenType listenType, int i) {

        }

        @Override
        public void onHotwordChange(HotwordState hotwordState, HotwordType hotwordType, String s) {

        }
    };
}
