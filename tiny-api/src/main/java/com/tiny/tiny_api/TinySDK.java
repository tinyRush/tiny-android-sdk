package com.tiny.tiny_api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Patterns;

/********************************************************
 * File Name : TinySDK.java
 * Author : ManhNV
 * Date : 2017-09-27
 * Description: 
 * Last-modified by : ManhNV
 * Last-modified : 2017-09-27
 ********************************************************/


public class TinySDK {
    final static String BASEURL_PROPERTY = "com.tinyrush.sdk.baseURL";

    public static synchronized void initialize(
            Context applicationContext) {
        loadDefaultsFromMetadata(applicationContext);
    }


    static void loadDefaultsFromMetadata(Context context) {
        if (context == null) {
            return;
        }
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return;
        }

        if (ai == null || ai.metaData == null) {
            return;
        }

        String baseURLString = ai.metaData.getString(BASEURL_PROPERTY);
        if (Patterns.WEB_URL.matcher(baseURLString).matches()) {
            TinyBuilder.getInstance().initBaseURL(baseURLString);
        } else {
            throw new IllegalArgumentException(
                    "Base URL not valid.");
        }


    }
}
