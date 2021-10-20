package com.nuwarobotics.example.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nuwarobotics.example.R;

/**
 * NUWA Authorization Example
 * Supported Software Version : 
 * 1. AIR-H200 1.4965.100JP.1
 * 2. AIR-H200 1.4962.10000.1
 *
 * This is a "NUWA 3rd App Active Code Authorization" service on Kebbi.
 * 3rd app could use Authorization interface to check is active code valid.
 * It allow 3rd company upload app to Nuwa Store for free download, and get(apply) active code for app use permission.
 *
 * Please contact NUWA Sales for more "APP Active Code" Information.
 * NOTICE : This Interface not work if no valid active code.
 *
 * 3rd Company Develop Step :
 * 1. Contact NUWA sales to apply "Active Code Authorization" service.
 * For technical, you should provide package name\company name
 * 2. NUWA establish 3rd company information, and setup\generate active code. (each code only allow active on one device)
 * 3. 3rd Company integrate this interface to app.
 * 4. 3rd Company apply app upload to Nuwa Store.
 * 5. 3rd Company keep active code and sale to customer.
 *
 *
 * We provide two way to check Authorization result
 * 1. Check by startActiviryForResult, and get result by onActivityResult
 * 2. Check by send intent to AUTH_SERVICE, and service will reply with intent broadcast
 *
 * Method 1 Sample Code - startActiviryForResult
 * lunch nuwa LicenceAuthActivity to confirm auth result.
 *      Intent activateIntent = new Intent();
 *      activateIntent.setComponent(new ComponentName(AUTH_SERVICE_PACKAGE, AUTH_SERVICE_ACTIVITY));
 *      // package name of 3rd app (this string should register to NUWA server)
 *      activateIntent.putExtra(PACKAGE_NAME, getApplication().getPackageName());
 *      // Use your company's name instead of "NUWA"
 *      activateIntent.putExtra(PROVIDER_NAME, "NUWA");
 *      startActivityForResult(activateIntent, REQUEST_CODE);
 * Get Authorization result from onActivityResult
 *      if (resultCode == RESULT_OK) {
 *          // Activation successful
 *          if (data != null) {
 *              // Get expiration time by solution 1
 *              String endtime = data.getStringExtra(KEY_AUTH_ENDTIME);
 *          }
 *      } else {
 *          // Activation failed by solution 1
 *      }
 *
 * Method 2 Sample Code - sendBroadcast
 * sendBroadcast to AUTH Service
 *      // Step2: Search authorization status
 *       Intent searchIntent = new Intent();
 *       searchIntent.setComponent(new ComponentName(AUTH_SERVICE_PACKAGE, AUTH_SERVICE_RECEIVER));
 *       searchIntent.setAction(ACTION_AUTH_QUERY);
 *       // package name
 *       searchIntent.putExtra(PACKAGE_NAME, getApplication().getPackageName());
 *       sendBroadcast(searchIntent);
 *
 * AUTH Service will reply result by broadcast following action
 *      ACTION_AUTH_SEARCH_ERROR = "com.nuwarobotics.service.home.action.AUTH_SEARCH_ERROR"
 *      ACTION_LICENCE_AUTH_ERROR = "com.nuwarobotics.service.home.action.LICENCE_AUTH_ERROR"
 *      ACTION_AUTH_SEARCH_RESULT = "com.nuwarobotics.service.home.action.AUTH_SEARCH_RESULT"
 *      ACTION_LICENCE_AUTH_RESULT = "com.nuwarobotics.service.home.action.LICENCE_AUTH_RESULT"
 *      //Get Get the result of query authorization status
 *      String status = intent.getStringExtra(KEY_AUTH_STATUS);//STATUS_ACTIVE、STATUS_INACTIVE、STATUS_OVERDUE
 *      // Get expiration time
 *      String endTime = intent.getStringExtra(KEY_AUTH_ENDTIME);
 *
 *
 *
 */
public class StartNuwaAuthorizationActivity extends AppCompatActivity implements View.OnClickListener {
    // Package of NUWA's service： Component PackageName
    private static final String AUTH_SERVICE_PACKAGE = "com.nuwarobotics.service.homeservice";
    // Activity of NUWA's service : Component ClassName
    private static final String AUTH_SERVICE_ACTIVITY = "com.nuwarobotics.service.home.auth.LicenceAuthActivity";
    // Broadcast of NUWA's service : Component ClassName
    private static final String AUTH_SERVICE_RECEIVER = "com.nuwarobotics.service.home.receiver.LicenceAuthReceiver";
    // Broadcast action of activating authorization
    private static final String ACTION_LICENCE_AUTH = "com.nuwarobotics.service.home.action.LICENCE_AUTH";
    // Broadcast action of querying authorization state
    private static final String ACTION_AUTH_SEARCH = "com.nuwarobotics.service.home.action.AUTH_SEARCH";
    // Broadcast action of result of search
    private static final String ACTION_AUTH_SEARCH_RESULT = "com.nuwarobotics.service.home.action.AUTH_SEARCH_RESULT";
    // Broadcast action of activating authorization callback result
    private static final String ACTION_LICENCE_AUTH_RESULT = "com.nuwarobotics.service.home.action.LICENCE_AUTH_RESULT";
    // Broadcast action for querying authorization status callback error information
    private static final String ACTION_AUTH_SEARCH_ERROR = "com.nuwarobotics.service.home.action.AUTH_SEARCH_ERROR";
    // Broadcast action of activating authorization callback error information
    private static final String ACTION_LICENCE_AUTH_ERROR = "com.nuwarobotics.service.home.action.LICENCE_AUTH_ERROR";
    // parameter1(package name).
    // Please replace to your package name which need query authorization status.
    private static final String PACKAGE_NAME = "pkg_name";
    // parameter2(name of third-party application service provider)
    // Please replace to your package name which need query authorization status.
    private static final String PROVIDER_NAME = "provider_name";
    // parameter3(Authorization code)
    private static final String AUTH_CODE = "auth_code";
    // parameter4(Flag of search only)
    private static final String SEARCH_ONLY = "search_only";
    // KEY of authorization status in authorization result
    private static final String KEY_AUTH_STATUS = "auth_status";
    // KEY of the authorization expiration time in the authorization result
    private static final String KEY_AUTH_ENDTIME = "auth_endtime";
    // KEY of authorization error message
    private static final String KEY_AUTH_ERROR_MESSAGE = "error_message";
    // authorization status
    private static final String STATUS_ACTIVE = "activated";
    private static final String STATUS_INACTIVE = "inactivated";
    private static final String STATUS_OVERDUE = "overdue";
    private static final int SEARCH_REQUEST_CODE = 0xcc;
    private static final int REQUEST_CODE = 0xdd;

    private EditText etCode;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_nuwa_authorization);
        findViewById(R.id.ib_btn_quit).setOnClickListener(this);
        findViewById(R.id.btn_start_auth).setOnClickListener(this);
        findViewById(R.id.btn_start_auth_search).setOnClickListener(this);
        findViewById(R.id.btn_get_auth).setOnClickListener(this);
        findViewById(R.id.btn_set_auth_broadcast).setOnClickListener(this);
        etCode = findViewById(R.id.et_auth_input);
        tvResult = findViewById(R.id.tv_auth_result);
        // Solution 2 Step 1: Register broadcast to receive result
        IntentFilter filter = new IntentFilter();
        // Search authorization status
        // Register broadcast and add two actions:ACTION_AUTH_SEARCH_RESULT & ACTION_AUTH_SEARCH_ERROR
        filter.addAction(ACTION_AUTH_SEARCH_RESULT);
        filter.addAction(ACTION_AUTH_SEARCH_ERROR);
        // Activate authorization
        // Register broadcast and add two actions:ACTION_LICENCE_AUTH_RESULT & ACTION_LICENCE_AUTH_ERROR
        filter.addAction(ACTION_LICENCE_AUTH_RESULT);
        filter.addAction(ACTION_LICENCE_AUTH_ERROR);
        registerReceiver(resultReceiver, filter);
    }

    // The BroadcastReceiver of result
    BroadcastReceiver resultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                String action = intent.getAction();
                // Step4: deal the result of search authorization status or activate authorization
                if (ACTION_AUTH_SEARCH_RESULT.equals(action)) {
                    // Get the result of query authorization status
                    String status = intent.getStringExtra(KEY_AUTH_STATUS);
                    // Get expiration time
                    String endTime = intent.getStringExtra(KEY_AUTH_ENDTIME);
                    tvResult.setText(getString(R.string.start_auth_example_result, status, endTime));
                    if (!TextUtils.isEmpty(status)) {
                        switch (status) {
                            case STATUS_ACTIVE:
                                // activated
                                break;
                            case STATUS_INACTIVE:
                                // inactivated
                                break;
                            case STATUS_OVERDUE:
                                // Expired
                                break;
                        }
                    }
                } else if (ACTION_LICENCE_AUTH_RESULT.equals(action)) {
                    // Get the result of activation authorization
                    String status = intent.getStringExtra(KEY_AUTH_STATUS);
                    // Get expiration time
                    String endTime = intent.getStringExtra(KEY_AUTH_ENDTIME);
                    tvResult.setText(getString(R.string.start_auth_example_result, status, endTime));
                    if (!TextUtils.isEmpty(status)) {
                        switch (status) {
                            case STATUS_ACTIVE:
                                // Activation successful
                                break;
                            case STATUS_INACTIVE:
                                // Activation failed
                                break;
                            case STATUS_OVERDUE:
                                // Expired
                                break;
                        }
                    }
                } else if (ACTION_AUTH_SEARCH_ERROR.equals(action)) {
                    // Get the error message of query authorization status
                    String err = intent.getStringExtra(KEY_AUTH_ERROR_MESSAGE);
                    tvResult.setText("Search Error: " + err);
                } else if (ACTION_LICENCE_AUTH_ERROR.equals(action)) {
                    // Get the error message of activation authorization
                    String err = intent.getStringExtra(KEY_AUTH_ERROR_MESSAGE);
                    tvResult.setText("Activate Error: " + err);
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        tvResult.setText("");
        switch (view.getId()) {
            case R.id.ib_btn_quit:
                finish();
                break;
            case R.id.btn_start_auth_search:// Solution 1 Step1: Search authorization status
                Intent searchIntent1 = new Intent();
                searchIntent1.setComponent(new ComponentName(AUTH_SERVICE_PACKAGE, AUTH_SERVICE_ACTIVITY));
                searchIntent1.putExtra(PACKAGE_NAME, getApplication().getPackageName());
                searchIntent1.putExtra(PROVIDER_NAME, "NUWA");
                // *Note that this parameter must be transmitted
                searchIntent1.putExtra(SEARCH_ONLY, true);
                startActivityForResult(searchIntent1, SEARCH_REQUEST_CODE);
                break;
            case R.id.btn_start_auth:// Solution 1 Step2: Launch NUWA Authorization
                // Call up the authorization page to activate the authorization
                Intent activateIntent = new Intent();
                activateIntent.setComponent(new ComponentName(AUTH_SERVICE_PACKAGE, AUTH_SERVICE_ACTIVITY));
                // package name
                activateIntent.putExtra(PACKAGE_NAME, getApplication().getPackageName());
                // Use your company's name instead of "NUWA"
                activateIntent.putExtra(PROVIDER_NAME, "NUWA");
                startActivityForResult(activateIntent, REQUEST_CODE);
                break;
            case R.id.btn_get_auth:
                // Solution 2 Step2: Search authorization status
                // Send query broadcast, note that the package name must be transmitted
                Intent searchIntent = new Intent();
                searchIntent.setComponent(new ComponentName(AUTH_SERVICE_PACKAGE, AUTH_SERVICE_RECEIVER));
                searchIntent.setAction(ACTION_AUTH_SEARCH);
                // package name
                searchIntent.putExtra(PACKAGE_NAME, getApplication().getPackageName());
                sendBroadcast(searchIntent);
                break;
            case R.id.btn_set_auth_broadcast:// Solution 2 Step3: Activate authorization
                // Use your own authorization page to activate authorization
                // Send the activation authorization broadcast at the appropriate time, and transmit the application package name and authorization code
                String code = etCode.getText().toString().trim();
                if (!checkCode(code)) {
                    Toast.makeText(this, getString(R.string.start_auth_example_activate_input_tip), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent activateIntent2 = new Intent();
                activateIntent2.setComponent(new ComponentName(AUTH_SERVICE_PACKAGE, AUTH_SERVICE_RECEIVER));
                activateIntent2.setAction(ACTION_LICENCE_AUTH);
                // package name
                activateIntent2.putExtra(PACKAGE_NAME, getApplication().getPackageName());
                // authorization code
                activateIntent2.putExtra(AUTH_CODE, code);
                sendBroadcast(activateIntent2);
                break;
        }
    }

    /**
     * The specification of the NUWA authorization code must be "16 digits, including both uppercase letters and numbers"
     */
    private boolean checkCode(String code) {
        if (TextUtils.isEmpty(code.trim())) {
            return false;
        }
        return code.matches(".*?[A-Z]+.*?")
                && code.matches(".*?[0-9]+.*?")
                && code.length() == 16;
    }

    /**
     * Example of receive Activation result
     * @param requestCode
     * @param resultCode RESULT_OK if query or auth success, other value means fail.
     * @param data intent format which include auth data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Activation successful
                if (data != null) {
                    // Get expiration time by solution 1
                    String authStatus = data.getStringExtra(KEY_AUTH_STATUS);
                    String endtime = data.getStringExtra(KEY_AUTH_ENDTIME);
                }
            } else {
                // Activation failed by solution 1
                if (data != null) {
                    // This value may be null
                    String err = data.getStringExtra(KEY_AUTH_ERROR_MESSAGE);
                }
            }
        }  else if (requestCode == SEARCH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Query successful by solution1
                if (data != null) {
                    // Get the result of query authorization status
                    String authStatus = data.getStringExtra(KEY_AUTH_STATUS);
                    // This value may be null
                    String endtime = data.getStringExtra(KEY_AUTH_ENDTIME);
                }
            } else {
                // Query failed by solution1
                if (data != null) {
                    // This value may be null
                    String err = data.getStringExtra(KEY_AUTH_ERROR_MESSAGE);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister receiver
        unregisterReceiver(resultReceiver);
    }
}
