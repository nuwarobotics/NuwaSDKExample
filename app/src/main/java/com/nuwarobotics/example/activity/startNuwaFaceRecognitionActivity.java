package com.nuwarobotics.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nuwarobotics.example.R;
import com.nuwarobotics.example.motion.base.BaseAppCompatActivity;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventListener;

/**
 * Allow developer call nuwa AddFamily app to add a member
 * Target SDK : 2.0.0.08
 */
public class startNuwaFaceRecognitionActivity extends BaseAppCompatActivity {
    private final static String TAG = "startNuwaFaceRecognitionActivity";

    //nuwa general style of close button
    ImageButton mCloseBtn;
    Button mStartDemo;
    EditText mInputNameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addface);

        mInputNameEdit = findViewById(R.id.input_name);

        mCloseBtn = findViewById(R.id.imgbtn_quit);
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        mStartDemo = findViewById(R.id.btn_start);
        mStartDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mInputNameEdit.getText().toString();
                if(!TextUtils.isEmpty(name)){
                    //Example of Skip input name step, start recognition directly
                    lunchFaceRecogWithName(name);
                }else{
                    //Example of start full process of add face to system contact
                    lunchFaceRecog();

                }
            }
        });



    }

    @Override
    protected int getLayoutRes(){
        return R.layout.activity_addface;
    }

    @Override
    protected int getToolBarTitleRes(){
        return R.string.start_face_recognition_example_title;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void lunchFaceRecog() {
        //Example of start full process of add face to system contact
        //IMPORTANT : ONLY Support on 2020/9 later Robot upgrade
        Intent intent = new Intent("com.nuwarobotics.action.FACE_REC");
        intent.setPackage("com.nuwarobotics.app.facerecognition2");
        intent.putExtra("EXTRA_3RD_REC_ONCE", true);
        startActivityForResult(intent, ACTIVITY_FACE_RECOGNITION);
    }
    private void lunchFaceRecogWithName(String name) {
        //Example of Skip input name step, start recognition directly
        //IMPORTANT : ONLY Support on 2020/10 later Robot upgrade
        Intent intent = new Intent("com.nuwarobotics.action.FACE_REC");
        intent.setPackage("com.nuwarobotics.app.facerecognition2");
        intent.putExtra("EXTRA_3RD_REC_ONCE", true);
        intent.putExtra("EXTRA_3RD_CONFIG_NAME", name);
        startActivityForResult(intent, ACTIVITY_FACE_RECOGNITION);
    }
    private final int ACTIVITY_FACE_RECOGNITION = 1;
    private final int ACTIVITY_FACE_RECOGNITION_ERROR = 2;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult, requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (resultCode > 0) {
            switch (resultCode) {
                case ACTIVITY_FACE_RECOGNITION:
                    Long mFaceID = data.getLongExtra("EXTRA_RESULT_FACEID", 0);
                    String mName = data.getStringExtra("EXTRA_RESULT_NAME");
                    String log_string = "onActivityResult, faceid=" + mFaceID + ", nickname=" + mName ;
                    Log.d(TAG, log_string);
                    Toast.makeText(this,log_string,Toast.LENGTH_LONG).show();
                    break;
                case ACTIVITY_FACE_RECOGNITION_ERROR:
                    String msg = data.getStringExtra("ERROR_MSG");
                    Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
                    break;
            }
        } else {
            Log.d(TAG, "unexception exit");
        }
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
