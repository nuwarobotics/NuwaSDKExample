package com.nuwarobotics.example.util;

import android.util.Log;

import com.nuwarobotics.example.BuildConfig;

public final class Logger {
    private static final String TAG = "NuwaSDKExampleMotion";

    private static boolean ENABLE_STACK_INFO = true;

    private Logger() {
    }

    private static StackTraceElement getTargetStackTraceElement() {
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            boolean isLogMethod = stackTrace.getClassName().equals(Logger.class.getName());
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTrace;
                break;
            }

            shouldTrace = isLogMethod;
        }

        return targetStackTrace;
    }

    private static String getDefaultTag(StackTraceElement stackTrace) {
        return TAG;
    }

    public static void v(String msg) {
        v(null, msg, null);
    }

    public static void v(String msg, Throwable tr) {
        v(null, msg, tr);
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            StackTraceElement stackTrace = getTargetStackTraceElement();

            if (tag == null) {
                tag = getDefaultTag(stackTrace);
            }

            String stackInfo = "";
            if (ENABLE_STACK_INFO) {
                stackInfo = stackTrace.getMethodName()
                        + "(" + stackTrace.getFileName() + ":" + stackTrace.getLineNumber() + ") ";
            }
            msg = stackInfo + msg;
            Log.v(tag, msg, tr);
        }
    }

    public static void i(String msg) {
        i(null, msg, null);
    }

    public static void i(String msg, Throwable tr) {
        i(null, msg, tr);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            StackTraceElement stackTrace = getTargetStackTraceElement();

            if (tag == null) {
                tag = getDefaultTag(stackTrace);
            }

            String stackInfo = "";
            if (ENABLE_STACK_INFO) {
                stackInfo = stackTrace.getMethodName()
                        + "(" + stackTrace.getFileName() + ":" + stackTrace.getLineNumber() + ") ";
            }
            msg = stackInfo + msg;
            Log.i(tag, msg, tr);
        }
    }

    public static void d(String msg) {
        d(null, msg, null);
    }

    public static void d(String msg, Throwable tr) {
        d(null, msg, tr);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            StackTraceElement stackTrace = getTargetStackTraceElement();

            if (tag == null) {
                tag = getDefaultTag(stackTrace);
            }

            String stackInfo = "";
            if (ENABLE_STACK_INFO) {
                stackInfo = stackTrace.getMethodName()
                        + "(" + stackTrace.getFileName() + ":" + stackTrace.getLineNumber() + ") ";
            }
            msg = stackInfo + msg;
            Log.d(tag, msg, tr);
        }
    }

    public static void w(String msg) {
        w(null, msg, null);
    }

    public static void w(String msg, Throwable tr) {
        w(null, msg, tr);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        StackTraceElement stackTrace = getTargetStackTraceElement();

        if (tag == null) {
            tag = getDefaultTag(stackTrace);
        }

        String stackInfo = "";
        if (ENABLE_STACK_INFO) {
            stackInfo = stackTrace.getMethodName()
                    + "(" + stackTrace.getFileName() + ":" + stackTrace.getLineNumber() + ") ";
        }
        msg = stackInfo + msg;
        Log.w(tag, msg, tr);
    }

    public static void e(String msg) {
        e(null, msg, null);
    }

    public static void e(String msg, Throwable tr) {
        e(null, msg, tr);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        StackTraceElement stackTrace = getTargetStackTraceElement();

        if (tag == null) {
            tag = getDefaultTag(stackTrace);
        }

        String stackInfo = "";
        if (ENABLE_STACK_INFO) {
            stackInfo = stackTrace.getMethodName()
                    + "(" + stackTrace.getFileName() + ":" + stackTrace.getLineNumber() + ") ";
        }
        msg = stackInfo + msg;
        Log.e(tag, msg, tr);
    }

}
