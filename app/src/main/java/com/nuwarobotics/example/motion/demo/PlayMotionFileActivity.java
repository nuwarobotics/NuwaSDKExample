package com.nuwarobotics.example.motion.demo;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.nuwarobotics.example.R;
import com.nuwarobotics.example.motion.base.BaseAppCompatActivity;
import com.nuwarobotics.example.util.FileUtil;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventCallback;

/**
 * This example present how to play app preload fst motion file.
 * API : public void motionPlay (String motion, boolean show, String path)
 * JavaDoc : https://developer-docs.nuwarobotics.com/sdk/javadoc/reference/com/nuwarobotics/service/agent/NuwaRobotAPI.html#motionPlay(java.lang.String,%20boolean,%20java.lang.String)
 *
 * Development Step :
 * 1. preload your fst file to app/assets/motion_bin folder
 * 2. when app initial, clone fst file to shared storage
 * 3. use motionPlay (String motion, boolean show, String path) API to play motion
 *
 */
public class PlayMotionFileActivity extends BaseAppCompatActivity {
    private NuwaRobotAPI mRobotAPI;
    private IClientId mClientId;

    // The fst file which preloaded in app/assets
    private final String MOTION_SAMPLE_1 = "example_fst_approve";
    private final String MOTION_SAMPLE_2 = "example_fst_touching";
    private final String EXTERNAL_FOLDER = "MyAssets";

    private TextView mTexPlayStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTexPlayStatus = findViewById(R.id.play_status);


        //Step 1 : Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this, mClientId);
        mRobotAPI.registerRobotEventListener(robotEventCallback); //listen callback of robot service event

        //Step 1.1 : prepare preload assets
        //Initial external assets folder
        String destPath = FileUtil.createExternalAssetFolder(EXTERNAL_FOLDER);
        //Clone preloaded fst to shared external storage
        FileUtil.copyAssetsToDst(this, "motion_bin", destPath);

        Button btn = findViewById(R.id.btn_playmotion);
        btn.setOnClickListener(v->{
            mTexPlayStatus.setText("");

            //Step 2 : Play local fst motion
            //https://developer-docs.nuwarobotics.com/sdk/javadoc/reference/com/nuwarobotics/service/agent/NuwaRobotAPI.html#motionPlay(java.lang.String,%20boolean,%20java.lang.String)
            mRobotAPI.motionPlay(MOTION_SAMPLE_1, false, Environment.getExternalStorageDirectory()+"/"+EXTERNAL_FOLDER);
        });

    }

    private void showEventMsg(String status){
        runOnUiThread(()->{
            mTexPlayStatus.append(status);
            mTexPlayStatus.append("\n");
            Log.d(TAG, status);
        });

    }

    private RobotEventCallback robotEventCallback = new RobotEventCallback() {
        @Override
        public void onStartOfMotionPlay(String s) {
            showEventMsg("Start Playing Motion...");
        }

        @Override
        public void onStopOfMotionPlay(String s) {
            showEventMsg("Stop Playing Motion...");
        }

        @Override
        public void onCompleteOfMotionPlay(String s) {
            showEventMsg("Play Motion Complete!!!");

            //Step 3 : If (the parameter of motionPlay)auto_fadein is ture,
            // the transparent view must be closed after motion is complete, error or other case.
            if(mRobotAPI != null){
                mRobotAPI.hideWindow(true);
            }
        }

        @Override
        public void onPlayBackOfMotionPlay(String s) {
            showEventMsg("Playing Motion...");
        }

        @Override
        public void onErrorOfMotionPlay(int i) {
            showEventMsg("When playing Motion, error happen!!! error code: " + i);

            if(mRobotAPI != null){
                mRobotAPI.hideWindow(true);
            }
        }
    };

    @Override
    protected void onStop(){
        super.onStop();

        //Step 4 : Release robotAPI before closing activity
        if(mRobotAPI != null){
            mRobotAPI.release();
        }
    }

    @Override
    protected int getLayoutRes(){
        return R.layout.activity_playmotion;
    }

    @Override
    protected int getToolBarTitleRes(){
        return R.string.lbl_example_2;
    }
}
