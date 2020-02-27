package com.nuwarobotics.example.motion.demo;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.nuwarobotics.example.R;
import com.nuwarobotics.example.motion.base.BaseAppCompatActivity;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventCallback;

public class ControlMotionActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private NuwaRobotAPI mRobotAPI;
    private IClientId mClientId;

    private TextView mTexPlayStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUIComponents();

        //Step 1 : Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this, mClientId);
        mRobotAPI.registerRobotEventListener(robotEventCallback); //listen callback of robot service event

    }

    private RobotEventCallback robotEventCallback = new RobotEventCallback() {
        @Override
        public void onStartOfMotionPlay(String motion) {
            showEventMsg("[Event]Start playing Motion... ,Motion: " + motion);
        }

        @Override
        public void onStopOfMotionPlay(String motion) {
            showEventMsg("[Event]Stop playing Motion... ,Motion: " + motion);
        }

        @Override
        public void onCompleteOfMotionPlay(String motion) {
            showEventMsg("[Event]Playing Motion is complete!!! Motion: " + motion);
        }

        @Override
        public void onPlayBackOfMotionPlay(String motion) {
            showEventMsg("[Event]Playing Motion... ,Motion: " + motion);
        }

        @Override
        public void onErrorOfMotionPlay(int i) {
            showEventMsg("[Event]When playing Motion, error happen!!! error code: " + i);
        }

        @Override
        public void onPauseOfMotionPlay(String motion) {
            showEventMsg("[Event]Pausing Motion... ,Motion: " + motion);
        }

        @Override
        public void onPrepareMotion(boolean isError, String motion, float duration) {
            showEventMsg("[Event]Prepare status, isError: " + isError + " ,Motion: " + motion
                + " ,duration: " + duration);
        }

    };

    @Override
    protected void onStop(){
        super.onStop();

        //Step 3 : Release robotAPI before closing activity
        if(mRobotAPI != null){
            mRobotAPI.release();
        }
    }

    @Override
    protected int getLayoutRes(){
        return R.layout.activity_controlmotion;
    }

    @Override
    protected int getToolBarTitleRes(){
        return R.string.lbl_example_3;
    }

    private void showEventMsg(String status){
        runOnUiThread(()->{
            mTexPlayStatus.append(status);
            mTexPlayStatus.append("\n");
            Log.d(TAG, status);
        });

    }

    private void initUIComponents(){
        mTexPlayStatus = findViewById(R.id.play_status);
        mTexPlayStatus.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTexPlayStatus.setOnClickListener(this);


        int[] btnResIdList = {
                R.id.btn_play,
                R.id.btn_pause,
                R.id.btn_resume,
                R.id.btn_stop,
        };

        for(int resId : btnResIdList){
            Button btn = findViewById(resId);

            if(btn != null){
                btn.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v){
        //Step 2 : Setup the click action of  "Play/Pause/Resume/Stop motion"
        //Play motion without transparent view for the demo of other actions
        switch(v.getId()){
            case R.id.btn_play:
                showEventMsg("[Click Button]Play");
                PopupMenu popup = new PopupMenu(ControlMotionActivity.this, findViewById(R.id.btn_play));
                popup.getMenuInflater().inflate(R.menu.popup_menu_querymotions, popup.getMenu());
                popup.getMenu().clear();

                String[] itemList = {
                        "666_RE_Bye",
                        "666_TA_LookRL",
                        "666_PE_Killed",
                        "666_DA_Scratching",
                        "666_IM_Rooster",
                        "666_TA_LookLR",
                        "666_PE_PlayGuitar",
                        "666_DA_PickUp"};

                for(String item : itemList) {
                    popup.getMenu().add(item);
                }

                popup.setOnMenuItemClickListener((item)->{
                    mRobotAPI.motionStop(true);
                    mRobotAPI.motionPlay(item.getTitle().toString(), false);
                    return true;
                });

                popup.show();

                break;
            case R.id.btn_pause:
                showEventMsg("[Click Button]Pause");
                mRobotAPI.motionPause();
                break;
            case R.id.btn_resume:
                showEventMsg("[Click Button]Resume");
                mRobotAPI.motionResume();
                break;
            case R.id.btn_stop:
                showEventMsg("[Click Button]Stop");
                mRobotAPI.motionStop(true);
                break;
            case R.id.play_status:
                mTexPlayStatus.setText("");
            default:
                Log.d(TAG, "Can't find the clicking action of view!!!");
        }
    }

}



