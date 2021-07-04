package com.nuwarobotics.example.motor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.nuwarobotics.example.R;
import com.nuwarobotics.example.util.Logger;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventCallback;

public class MotorControlActivity extends AppCompatActivity {
    NuwaRobotAPI mRobotAPI;
    IClientId mClientId;

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

    private void initView(){
        setContentView(R.layout.activity_motorcontrol);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(this.getClass().getCanonicalName());
        }

        findViewById(R.id.button_motor_neck_y_go).setOnClickListener(v -> {
            try {
                mRobotAPI.ctlMotor(NuwaRobotAPI.MOTOR_NECK_Y, Float.parseFloat(((EditText) findViewById(R.id.edit_motor_neck_y)).getText().toString()), 45);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button_motor_neck_z_go).setOnClickListener(v -> {
            try {
                mRobotAPI.ctlMotor(NuwaRobotAPI.MOTOR_NECK_Z, Float.parseFloat(((EditText) findViewById(R.id.edit_motor_neck_z)).getText().toString()), 45);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button_motor_right_shoulder_z_go).setOnClickListener(v -> {
            try {
                mRobotAPI.ctlMotor(NuwaRobotAPI.MOTOR_RIGHT_SHOULDER_Z, Float.parseFloat(((EditText) findViewById(R.id.edit_motor_right_shoulder_z)).getText().toString()), 45);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button_motor_right_shoulder_y_go).setOnClickListener(v -> {
            try {
                mRobotAPI.ctlMotor(NuwaRobotAPI.MOTOR_RIGHT_SHOULDER_Y, Float.parseFloat(((EditText) findViewById(R.id.edit_motor_right_shoulder_y)).getText().toString()), 45);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button_motor_right_shoulder_x_go).setOnClickListener(v -> {
            try {
                mRobotAPI.ctlMotor(NuwaRobotAPI.MOTOR_RIGHT_SHOULDER_X, Float.parseFloat(((EditText) findViewById(R.id.edit_motor_right_shoulder_x)).getText().toString()), 45);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button_motor_right_elbow_y_go).setOnClickListener(v -> {
            try {
                mRobotAPI.ctlMotor(NuwaRobotAPI.MOTOR_RIGHT_ELBOW_Y, Float.parseFloat(((EditText) findViewById(R.id.edit_motor_right_elbow_y)).getText().toString()), 45);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button_motor_left_shoulder_z_go).setOnClickListener(v -> {
            try {
                mRobotAPI.ctlMotor(NuwaRobotAPI.MOTOR_LEFT_SHOULDER_Z, Float.parseFloat(((EditText) findViewById(R.id.edit_motor_left_shoulder_z)).getText().toString()), 45);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button_motor_left_shoulder_y_go).setOnClickListener(v -> {
            try {
                mRobotAPI.ctlMotor(NuwaRobotAPI.MOTOR_LEFT_SHOULDER_Y, Float.parseFloat(((EditText) findViewById(R.id.edit_motor_left_shoulder_y)).getText().toString()), 45);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button_motor_left_shoulder_x_go).setOnClickListener(v -> {
            try {
                mRobotAPI.ctlMotor(NuwaRobotAPI.MOTOR_LEFT_SHOULDER_X, Float.parseFloat(((EditText) findViewById(R.id.edit_motor_left_shoulder_x)).getText().toString()), 45);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button_motor_left_elbow_y_go).setOnClickListener(v -> {
            try {
                mRobotAPI.ctlMotor(NuwaRobotAPI.MOTOR_LEFT_ELBOW_Y, Float.parseFloat(((EditText) findViewById(R.id.edit_motor_left_elbow_y)).getText().toString()), 45);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
}
