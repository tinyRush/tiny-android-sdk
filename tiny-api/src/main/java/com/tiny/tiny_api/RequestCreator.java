package com.tiny.tiny_api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by meo on 9/19/17.
 */

public class RequestCreator {

    public static final String REQUEST_GET = "REQUEST_GET";
    public static final String REQUEST_PUT = "REQUEST_PUT";
    public static final String REQUEST_POST = "REQUEST_POST";
    public static final String REQUEST_DELETE = "REQUEST_DELETE";
    public static final String REQUEST_PATCH = "REQUEST_PATCH";

    private FormBody.Builder bodyBuilder = null;
    private Headers.Builder headers = null;
    private MultipartBody.Builder multipartBulder = null;
    private HttpUrl url;
    private OkHttpClient client = null;
    private JSONObject jsonObject;
    private final String method;

    public RequestCreator(HttpUrl url, String method) {
        this.method = method;
        this.url = url;
        this.client = new OkHttpClient();
    }

    public RequestCreator addInterceptor(Interceptor interceptor) {
        this.client = client.newBuilder().addInterceptor(interceptor).build();
        return this;
    }

    public RequestCreator addQuery(String key, Object value) {
        url.newBuilder().addQueryParameter(key, value.toString());
        return this;
    }

    public RequestCreator addQueries(HashMap<String, ?> hashMap) {
        Set set = hashMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            url.newBuilder().addQueryParameter(mentry.getKey().toString(), mentry.getValue().toString());
        }
        return this;
    }

    public RequestCreator addHeader(String key, Object value) {
        if (headers == null) {
            headers = new Headers.Builder();
        }
        headers.add(key, value.toString());
        return this;
    }

    public RequestCreator addHeaders(HashMap<String, ?> hashMap) {
        if (headers == null) {
            headers = new Headers.Builder();
        }
        Set set = hashMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            headers.add(mentry.getKey().toString(), mentry.getValue().toString());
        }
        return this;
    }

    public RequestCreator addField(String key, Object value) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        try {
            jsonObject.put(key, value.toString());
        } catch (JSONException e) {

        }
        return this;
    }

    public RequestCreator addFields(HashMap<String, ?> hashMap) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        try {
            Set set = hashMap.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                jsonObject.put(mentry.getKey().toString(), mentry.getValue().toString());
            }
        } catch (JSONException e) {

        }
        return this;
    }

    public RequestCreator addPart(String mediaType, String filename, String sourceFile) {
        if (multipartBulder == null) {
            multipartBulder = new MultipartBody.Builder();
            multipartBulder.setType(MultipartBody.FORM);
        }
        multipartBulder
                .addFormDataPart("uploaded_file", filename, RequestBody.create(MediaType.parse(mediaType), sourceFile));
        return this;
    }

    public RequestCreator addPart(String key, String value) {
        if (multipartBulder == null) {
            multipartBulder = new MultipartBody.Builder();
            multipartBulder.setType(MultipartBody.FORM);
        }
        multipartBulder
                .addFormDataPart(key, value);
        return this;
    }

    private void handleBody(Request.Builder request) {
        RequestBody requestBody = checkBody();
        switch (method) {
            case REQUEST_GET:
                request.get();
                break;
            case REQUEST_POST:
                if (bodyBuilder == null) {
                    bodyBuilder = new FormBody.Builder();
                }
                request.post(requestBody);
                break;
            case REQUEST_PUT:
                if (bodyBuilder == null) {
                    bodyBuilder = new FormBody.Builder();
                }
                request.put(requestBody);
                break;
            case REQUEST_PATCH:
                if (bodyBuilder == null) {
                    bodyBuilder = new FormBody.Builder();
                }
                request.patch(requestBody);
                break;
            case REQUEST_DELETE:
                request.delete();
                break;
        }
    }

    private RequestBody checkBody() {
        if (multipartBulder != null) {
            if (jsonObject != null) {
                multipartBulder.addPart(RequestBody.create(Tiny.JSON, jsonObject.toString()));
            } else {
                return multipartBulder.build();
            }
        } else {
            if (jsonObject != null) {
                return RequestBody.create(Tiny.JSON, jsonObject.toString());
            }
        }
        return null;
    }

    public void enqueue(final TinyCallback callback) {
        Request.Builder request = new Request.Builder();
        request.url(url);
        if (headers != null) request.headers(headers.build());
        handleBody(request);

        client.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onFailure(call, e);
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
            }
        });
    }

    public Object execute() throws IOException {
        Request.Builder request = new Request.Builder();
        request.url(url);
        if (headers != null) request.headers(headers.build());
        handleBody(request);
        try (Response response = client.newCall(request.build()).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}
