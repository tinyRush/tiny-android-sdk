package com.tiny.tiny_api;

import android.util.Patterns;

import okhttp3.HttpUrl;

/**
 * Created by meo on 9/15/17.
 */

public class Tiny {

    public static RequestCreator post(String path){
       return new RequestCreator(handleURL(path), RequestCreator.REQUEST_POST);
    }
    public static RequestCreator put(String path){
        return new RequestCreator(handleURL(path), RequestCreator.REQUEST_PUT);
    }
    public static RequestCreator patch(String path){
        return new RequestCreator(handleURL(path), RequestCreator.REQUEST_PATCH);
    }
    public static RequestCreator get(String path){
        return new RequestCreator(handleURL(path), RequestCreator.REQUEST_GET);
    }
    public static RequestCreator delete(String path){
        return new RequestCreator(handleURL(path), RequestCreator.REQUEST_DELETE);
    }


    private static HttpUrl handleURL(String path){
        HttpUrl url;

        if (Patterns.WEB_URL.matcher(path).matches()) {
            return HttpUrl.parse(path);
        } else {
            return HttpUrl.parse(TinyBuilder.baseUrl+path);
        }
    }

}
