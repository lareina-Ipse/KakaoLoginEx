package com.example.kakaologinex.util;

import android.util.Log;

import com.example.kakaologinex.config.ConfigData;

public class LogUtil {

    private static final String LOG_PREFIX = "### ";

    public static void i(String Tag, String message) {
        if (ConfigData.LOG_ENABLE) {
            Log.i(Tag, LOG_PREFIX + message);
        }
    }

    public static void d(String Tag, String message) {
        if (ConfigData.LOG_ENABLE) {
            Log.d(Tag, LOG_PREFIX + message);
        }
    }

    public static void w(String Tag, String message) {
        if (ConfigData.LOG_ENABLE) {
            Log.w(Tag, LOG_PREFIX + message);
        }
    }

    public static void e(String Tag, String message) {
        if (ConfigData.LOG_ENABLE) {
            Log.e(Tag, LOG_PREFIX + message);
        }
    }
}
