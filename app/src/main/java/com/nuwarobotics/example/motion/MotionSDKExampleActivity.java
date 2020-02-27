package com.nuwarobotics.example.motion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nuwarobotics.example.motion.base.BaseAppCompatActivity;
import com.nuwarobotics.example.motion.demo.ControlMotionActivity;
import com.nuwarobotics.example.motion.demo.PlayMotionActivity;
import com.nuwarobotics.example.motion.demo.QueryMotionActivity;
import com.nuwarobotics.example.motion.demo.WindowControlWithMotionActivity;

import com.nuwarobotics.example.R;

/**
 * Motion SDK Example
 *
 */
public class MotionSDKExampleActivity extends BaseAppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int[] btnResIdList = {
                R.id.btn_example1,
                R.id.btn_example2,
                R.id.btn_example3,
                R.id.btn_example4
        };

        for(int resId : btnResIdList){
            Button btn = findViewById(resId);

            if(btn != null){
                btn.setOnClickListener(this);
            }
        }

    }

    private void launchActivity(Class clz){
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_example1:
                launchActivity(QueryMotionActivity.class);
                break;
            case R.id.btn_example2:
                launchActivity(PlayMotionActivity.class);
                break;
            case R.id.btn_example3:
                launchActivity(ControlMotionActivity.class);
                break;
            case R.id.btn_example4:
                launchActivity(WindowControlWithMotionActivity.class);
                break;
        }
    }

}
