package com.tiny.tiny_api.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tiny.tiny_api.TinySDK;
import com.tiny.tiny_api.exception.TinyException;

import java.util.Collection;

/**
 * File Name : Validate.java
 * Author : ManhNV
 * Date : 2017-09-06
 */


public class Validate {
    private static final String TAG = Validate.class.getName();
    private static final String NO_INTERNET_PERMISSION_REASON =
            "No internet permissions granted for the app, please add " +
                    "<uses-permission android:name=\"android.permission.INTERNET\" /> " +
                    "to your AndroidManifest.xml.";

    public static void notNull(Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException("Argument '" + name + "' cannot be null");
        }
    }

    public static <T> void notEmpty(Collection<T> container, String name) {
        if (container.isEmpty()) {
            throw new IllegalArgumentException("Container '" + name + "' cannot be empty");
        }
    }

    public static <T> void containsNoNulls(Collection<T> container, String name) {
        Validate.notNull(container, name);
        for (T item : container) {
            if (item == null) {
                throw new NullPointerException("Container '" + name +
                        "' cannot contain null values");
            }
        }
    }

    public static void containsNoNullOrEmpty(Collection<String> container, String name) {
        Validate.notNull(container, name);
        for (String item : container) {
            if (item == null) {
                throw new NullPointerException("Container '" + name +
                        "' cannot contain null values");
            }
            if (item.length() == 0) {
                throw new IllegalArgumentException("Container '" + name +
                        "' cannot contain empty values");
            }
        }
    }

    public static <T> void notEmptyAndContainsNoNulls(Collection<T> container, String name) {
        Validate.containsNoNulls(container, name);
        Validate.notEmpty(container, name);
    }


    public static void hasInternetPermissions(Context context, boolean shouldThrow) {
        Validate.notNull(context, "context");
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) ==
                PackageManager.PERMISSION_DENIED) {
            if (shouldThrow) {
                throw new IllegalStateException(NO_INTERNET_PERMISSION_REASON);
            } else {
                Log.w(TAG, NO_INTERNET_PERMISSION_REASON);
            }
        }
    }

    public static void sdkInitialized() {
        if (!TinySDK.isInitialized()) {
            throw new TinyException(
                    "The SDK has not been initialized, make sure to call " +
                            "SeedAppSDK.initialize() first.");
        }
    }
}
