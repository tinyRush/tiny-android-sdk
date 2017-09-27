package com.tiny.tiny_api;

import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by meo on 9/19/17.
 */

public class TinyBuilder {
    private static TinyBuilder singleton = null;
    static String baseUrl;
    static Headers headers;
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    public static TinyBuilder getInstance() {
        if (singleton == null) {
            synchronized (TinyBuilder.class) {
                if (singleton == null) {
                    singleton = new TinyBuilder();
                }
            }
        }
        return singleton;
    }

    public TinyBuilder initBaseURL(String urlString) {
        baseUrl = urlString;
        return singleton;
    }
}
