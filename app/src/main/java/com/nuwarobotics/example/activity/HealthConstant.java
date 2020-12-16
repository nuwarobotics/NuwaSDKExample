package com.nuwarobotics.example.activity;

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

    public final static int RESULT_CODE_CAL_SUCCESS = 24 ;

    public final static int FACE_RESULT_SUCCESS = 0;
    public final static int FACE_RESULT_FAIL_NO_DEVICE = -1;
    public final static int FACE_RESULT_MEASURE_TIMEOUT = -2;
    public final static int FACE_RESULT_USER_QUIT = -3;

    public final static String NUWA_BLE_SERVICE_PACKAGE = "com.nuwarobotics.app.nuwableservice";
    public final static String NUWA_BLE_SERVICE_ACTION = "com.nuwarobotics.action.NUWA_BLE_SERVICE";
}
