package com.tiny.tiny_api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by meo on 9/19/17.
 */

public class RequestCreator {

    public static final String REQUEST_GET="REQUEST_GET";
    public static final String REQUEST_PUT ="REQUEST_PUT";
    public static final String REQUEST_POST="REQUEST_POST";
    public static final String REQUEST_DELETE="REQUEST_DELETE";
    public static final String REQUEST_PATCH = "REQUEST_PATCH";

    private FormBody.Builder bodyBuilder;
    private Headers.Builder headers;
    private boolean async = true;

    private HttpUrl url;
    private OkHttpClient client = null;
    private JSONObject jsonObject;
    private final String method;
    private TinyCallback callback;

    public RequestCreator(HttpUrl url, String method){
        this.method = method;
        this.url = url;
        this.client = new OkHttpClient();
    }

//    RequestCreator add(String key, Object value){
//        if (bodyBuilder == null){
//            bodyBuilder = new FormBody.Builder();
//        }
//        bodyBuilder.add(key, value.toString());
//        return this;
//    }

//    RequestCreator addQuery(String key, Object value){
//        url.
//    }

    RequestCreator add(String key, Object value){
        if (jsonObject == null){
            jsonObject = new JSONObject();
        }
        try {
            jsonObject.put(key, value.toString());
        }catch (JSONException e){

        }

        return this;
    }

    RequestCreator addHeader(String key, Object value){
        if (headers == null){
            headers = new Headers.Builder();
        }
        headers.add(key, value.toString());
        return this;
    }

    RequestCreator isSync(boolean sync){
        this.async = !sync;
        return this;
    }

    RequestCreator onResponse(TinyCallback callback){
        this.callback = callback;
        return this;
    }

    public void enqueue(){
        Request.Builder request = new Request.Builder();
        request.url(url);
        if (headers!=null) request.headers(headers.build());
        switch (method){
            case REQUEST_GET:
                request.get();
                break;
            case REQUEST_POST:
                if(bodyBuilder == null){
                    bodyBuilder = new FormBody.Builder();
                }
                request.post(RequestBody.create(TinyBuilder.JSON, jsonObject.toString()));
                break;
            case REQUEST_PUT:
                if(bodyBuilder == null){
                    bodyBuilder = new FormBody.Builder();
                }
                request.put(RequestBody.create(TinyBuilder.JSON, jsonObject.toString()));
                break;
            case REQUEST_PATCH:
                if(bodyBuilder == null){
                    bodyBuilder = new FormBody.Builder();
                }
                request.patch(RequestBody.create(TinyBuilder.JSON, jsonObject.toString()));
                break;
            case REQUEST_DELETE:
                request.delete();
                break;
        }

        client.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null){
                    callback.onFailure(call,e);
                }
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (callback != null){
                    callback.onResponse(call,response);
                }
                Log.e("response",response.body().string());
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                }
            }
        });
    }

    public Object execute() throws IOException{
        Request.Builder request = new Request.Builder();
        request.url(url);
        if (headers!=null) request.headers(headers.build());
        switch (method){
            case REQUEST_GET:
                request.get();
                break;
            case REQUEST_POST:
                if(bodyBuilder == null){
                    bodyBuilder = new FormBody.Builder();
                }
                request.post(RequestBody.create(TinyBuilder.JSON, jsonObject.toString()));
                break;
            case REQUEST_PUT:
                if(bodyBuilder == null){
                    bodyBuilder = new FormBody.Builder();
                }
                request.put(RequestBody.create(TinyBuilder.JSON, jsonObject.toString()));
                break;
            case REQUEST_PATCH:
                if(bodyBuilder == null){
                    bodyBuilder = new FormBody.Builder();
                }
                request.patch(RequestBody.create(TinyBuilder.JSON, jsonObject.toString()));
                break;
            case REQUEST_DELETE:
                request.delete();
                break;
        }

        try (Response response = client.newCall(request.build()).execute()) {
            Log.e("response",response.body().string());
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}
