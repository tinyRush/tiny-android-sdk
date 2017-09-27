package com.tiny.tiny_api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.tiny.tiny_api.exception.TinyException;
import com.tiny.tiny_api.utils.StringUtils;
import com.tiny.tiny_api.utils.Validate;

/**
 * File Name : TinySDK.java
 * Author : ManhNV
 * Date : 2017-09-27
 */


public class TinySDK {
    private static final String URL_PROPERTY = "com.tiny.sdk.url";
    private static Context applicationContext;
    private static boolean initialized;
    private static String url;

    /**
     * Initialize.
     *
     * @param applicationContext the application context
     */
    public static void initialize(Context applicationContext) {
        if (initialized) {
            return;
        }

        Validate.notNull(applicationContext, "applicationContext");

        Validate.hasInternetPermissions(applicationContext, false);

        TinySDK.applicationContext = applicationContext;

        TinySDK.loadDefaultsFromMetadata(TinySDK.applicationContext);


        if (StringUtils.isEmpty(url)) {
            throw new TinyException("A valid url must be set in the " +
                    "AndroidManifest.xml " +
                    "before initializing the sdk.");
        }

        TinySDK.initialized = true;
    }

    private static void loadDefaultsFromMetadata(Context context) {
        if (context == null) {
            return;
        }

        ApplicationInfo ai;
        try {
            ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return;
        }

        if (ai == null || ai.metaData == null) {
            return;
        }

        if (url == null) {
            Object tempUrl = ai.metaData.get(URL_PROPERTY);
            if (tempUrl instanceof String) {
                String urlString = (String) tempUrl;
                if (urlString.contains("http://")) {
                    url = urlString;
                } else {
                    throw new TinyException("Url = %s is wrong format.", urlString);
                }
            } else {
                throw new TinyException(
                        "URL cannot be directly placed in the manifest." +
                                "They must be placed in the string resource file.");
            }
        }

    }

    /**
     * Is initialized boolean.
     *
     * @return the boolean
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Sets initialized.
     *
     * @param initialized the initialized
     */
    public static void setInitialized(boolean initialized) {
        TinySDK.initialized = initialized;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public static String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public static void setUrl(String url) {
        TinySDK.url = url;
    }

}
