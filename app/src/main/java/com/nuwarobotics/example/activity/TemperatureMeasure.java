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
 * Temperature Measure API example
 * This example present
 * - How to measure temperature and get result from NuwaHealth.
 * Target SDK : 2.1.0.02
 */
public class TemperatureMeasure extends BaseAppCompatActivity {
    private final static String TAG = "TemperatureMeasure";

    //nuwa general style of close button
    ImageButton mCloseBtn;
    Button mBtnDemoFlir;
    Button mBtnDemoFlirKebbiFace;
    Button mBtnDemoMelexis;
    TextView mTestResult;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_measure);
        mContext = this;

        mTestResult = findViewById(R.id.measure_result);

        mBtnDemoFlir = findViewById(R.id.btn_demo_flir);
        mBtnDemoFlir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.nuwarobotics.health.action.facerec");
                intent.putExtra(HealthConstant.EXTRA_PRECISE,0);
                intent.putExtra(HealthConstant.EXTRA_KEBBI_FACE,0);
                intent.putExtra(HealthConstant.EXTRA_REPORT,1);
                startActivityForResult(intent, HealthConstant.MEASURE_FLOW_CONTINUOUS_SINGLE);
            }
        });

        mBtnDemoFlirKebbiFace = findViewById(R.id.btn_demo_flir_kebbi_face);
        mBtnDemoFlirKebbiFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.nuwarobotics.health.action.facerec");
                intent.putExtra(HealthConstant.EXTRA_PRECISE,0);
                intent.putExtra(HealthConstant.EXTRA_KEBBI_FACE,1);
                intent.putExtra(HealthConstant.EXTRA_REPORT,1);
                startActivityForResult(intent, HealthConstant.MEASURE_FLOW_CONTINUOUS_SINGLE);
            }
        });

        mBtnDemoMelexis = findViewById(R.id.btn_demo_melexis);
        mBtnDemoMelexis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.nuwarobotics.health.action.facerec");
                intent.putExtra(HealthConstant.EXTRA_PRECISE,1);
                intent.putExtra(HealthConstant.EXTRA_REPORT,1);
                startActivityForResult(intent, HealthConstant.MEASURE_FLOW_CONTINUOUS_SINGLE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case HealthConstant.FACE_RESULT_SUCCESS:
                String name = data.getStringExtra(HealthConstant.INTENT_DATA_FACE);
                String temperature = data.getStringExtra(HealthConstant.INTENT_DATA_TEMPERATURE);
                String time = data.getStringExtra(HealthConstant.INTENT_DATA_TIME);
                String mask = data.getStringExtra(HealthConstant.INTENT_DATA_MASK);
                if(name.equals("")){
                    mTestResult.setText("Name: Unknown" + "\nTemperature: " + temperature + "°C" + "\nmask:" + mask +"\n" +
                            "Time:" + time);
                }
                else {
                    mTestResult.setText("Name:" + name + "\nTemperature: " + temperature + "°C" + "\nmask:" + mask + "\n" +
                            "Time:" + time);
                }
                break;
            case HealthConstant.FACE_RESULT_FAIL_NO_DEVICE:
                showDialog("Error", "Can't find BLE devcie");
                break;
            case HealthConstant.FACE_RESULT_MEASURE_TIMEOUT:
                showDialog("Error", "Can't find human face. Temperature measure timeout");
                break;
            case HealthConstant.FACE_RESULT_USER_QUIT:
                showDialog("Information", "User finish temperature procedure.");
                break;
            default:
                Log.d(TAG, "Unknown result for temperature measure result");
                break;
        }
    }

    private void showDialog(String title, String content) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(content)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

    public class HealthConstant {
        public final static String EXTRA_REQUEST_CODE = "requestCode";
        public final static String EXTRA_REPORT = "com.nuwarobotics.health.extra.report";
        public final static String EXTRA_PRECISE = "com.nuwarobotics.health.extra.precise";
        public final static String EXTRA_KEBBI_FACE = "com.nuwarobotics.health.extra.kebbi_face";

        public final static int MEASURE_FLOW_CONTINUOUS_SINGLE = 1;

        public final static String INTENT_DATA_FACE = "face_name";
        public final static String INTENT_DATA_TEMPERATURE = "face_temperature";
        public final static String INTENT_DATA_TIME = "face_time";
        public final static String INTENT_DATA_MASK = "mask";

        public final static int FACE_RESULT_SUCCESS = 0;
        public final static int FACE_RESULT_FAIL_NO_DEVICE = -1;
        public final static int FACE_RESULT_MEASURE_TIMEOUT  = -2;
        public final static int FACE_RESULT_USER_QUIT  = -3;
    }
}
