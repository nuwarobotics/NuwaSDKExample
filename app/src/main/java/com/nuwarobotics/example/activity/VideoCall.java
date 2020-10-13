package com.nuwarobotics.example.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Video Call example
 * This example present
 * - How to start video call
 * Target SDK : 2.1.0.02
 */
public class VideoCall extends BaseAppCompatActivity {
    private final static String TAG = "VideoCall";

    //nuwa general style of close button
    ImageButton mCloseBtn;
    Button mBtnDemo;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        mContext = this;

        mBtnDemo = findViewById(R.id.btn_start);
        mBtnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact_name = "John";//specific name of family contact
                Bundle mIntentBundle = new Bundle();
                mIntentBundle.putString("callee", contact_name);

                Intent intent = new Intent();
                intent.putExtras(mIntentBundle);
                intent.setClassName("com.nuwarobotics.app.videocall", "com.nuwarobotics.app.videocall.SignalingService");
                mContext.startService(intent);
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
}
