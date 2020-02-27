package com.nuwarobotics.example;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.nuwarobotics.example.util.AIPermissionRequest;
import com.nuwarobotics.example.util.ActivityBean;
import com.nuwarobotics.example.util.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Main extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "Main";
    private AIPermissionRequest mPermissionRequest;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Grant permission
        mPermissionRequest = new AIPermissionRequest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestMulti();
            Log.d(TAG, "request all needed　permissions");
        }


        ListView listView = (ListView) findViewById(R.id.activity_list);
        ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> item = new HashMap<String, Object>();

        //Fetch activity list
        ArrayList<ActivityBean> activityArrayList = ActivityUtils.pullXML(getApplicationContext(), "cfg_functions.xml");

        for (ActivityBean activityBean : activityArrayList) {
            item = new HashMap<>();
            try {
                item.put("activity_name", activityBean.getLabel());
                item.put("activity_class", Class.forName(getPackageName() + "." + activityBean.getName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            listItems.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"activity_name"}, new int[]{R.id.text_item});

        listView.setAdapter(adapter);
        listView.setDividerHeight(2);

        listView.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<?, ?> map = (HashMap<?, ?>) parent.getAdapter().getItem(position);
        Class<?> clazz = (Class<?>) map.get("activity_class");

        Intent it = new Intent(this, clazz);
        this.startActivity(it);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void requestMulti() {
        mPermissionRequest.requestMultiPermissions(this, mPermissionGrant);
    }

    private AIPermissionRequest.PermissionGrant mPermissionGrant = new AIPermissionRequest.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case AIPermissionRequest.CODE_READ_PHONE_STATE:
                    Toast.makeText(Main.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case AIPermissionRequest.CODE_RECORD_AUDIO:
                    Toast.makeText(Main.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case AIPermissionRequest.CODE_READ_EXTERNAL_STORAGE:
                    Toast.makeText(Main.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case AIPermissionRequest.CODE_WRITE_EXTERNAL_STORAGE:
                    Toast.makeText(Main.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

}
