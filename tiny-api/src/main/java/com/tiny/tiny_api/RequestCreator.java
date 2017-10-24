package com.tiny.tiny_api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

/**
 * Created by meo on 9/19/17.
 */

public class RequestCreator {

    public static final String REQUEST_GET = "REQUEST_GET";
    public static final String REQUEST_PUT = "REQUEST_PUT";
    public static final String REQUEST_POST = "REQUEST_POST";
    public static final String REQUEST_DELETE = "REQUEST_DELETE";
    public static final String REQUEST_PATCH = "REQUEST_PATCH";

    private FormBody.Builder mBodyBuilder = null;
    private Headers.Builder mHeaders = null;
    private MultipartBody.Builder mMultipartBulder = null;
    private HttpUrl mUrl;
    private OkHttpClient mClient = null;
    private JSONObject mJsonObject;
    private final String method;

    public RequestCreator(HttpUrl url, String method) {
        this.method = method;
        this.mUrl = url;
        this.mClient = new OkHttpClient();
    }

    public RequestCreator addInterceptor(Interceptor interceptor) {
        this.mClient = mClient.newBuilder().addInterceptor(interceptor).build();
        return this;
    }

    public RequestCreator addQuery(String key, Object value) {
        mUrl.newBuilder().addQueryParameter(key, value.toString());
        return this;
    }

    public RequestCreator addQueries(Map<String, ?> hashMap) {
        Set set = hashMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            mUrl.newBuilder().addQueryParameter(mentry.getKey().toString(), mentry.getValue().toString());
        }
        return this;
    }

    public RequestCreator addHeader(String key, Object value) {
        if (mHeaders == null) {
            mHeaders = new Headers.Builder();
        }
        mHeaders.add(key, value.toString());
        return this;
    }

    public RequestCreator addHeaders(Map<String, ?> hashMap) {
        if (mHeaders == null) {
            mHeaders = new Headers.Builder();
        }
        Set set = hashMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            mHeaders.add(mentry.getKey().toString(), mentry.getValue().toString());
        }
        return this;
    }

    public RequestCreator addField(String key, Object value) {
        if (mJsonObject == null) {
            mJsonObject = new JSONObject();
        }
        try {
            mJsonObject.put(key, value.toString());
        } catch (JSONException e) {

        }
        return this;
    }

    public RequestCreator addFields(Map<String, ?> hashMap) {
        if (mJsonObject == null) {
            mJsonObject = new JSONObject();
        }
        try {
            Set set = hashMap.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                mJsonObject.put(mentry.getKey().toString(), mentry.getValue().toString());
            }
        } catch (JSONException e) {

        }
        return this;
    }

    public RequestCreator addPart(String mediaType, String filename, String sourceFile) {
        if (mMultipartBulder == null) {
            mMultipartBulder = new MultipartBody.Builder();
            mMultipartBulder.setType(MultipartBody.FORM);
        }
        mMultipartBulder
                .addFormDataPart("uploaded_file", filename, RequestBody.create(MediaType.parse(mediaType), sourceFile));
        return this;
    }

    public RequestCreator addPart(RequestBody requestBody) {
        if (mMultipartBulder == null) {
            mMultipartBulder = new MultipartBody.Builder();
            mMultipartBulder.setType(MultipartBody.FORM);
        }
        mMultipartBulder
                .addPart(requestBody);
        return this;
    }

    public RequestCreator addPart(String key, String value) {
        if (mMultipartBulder == null) {
            mMultipartBulder = new MultipartBody.Builder();
            mMultipartBulder.setType(MultipartBody.FORM);
        }
        mMultipartBulder
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
                if (mBodyBuilder == null) {
                    mBodyBuilder = new FormBody.Builder();
                }
                request.post(requestBody);
                break;
            case REQUEST_PUT:
                if (mBodyBuilder == null) {
                    mBodyBuilder = new FormBody.Builder();
                }
                request.put(requestBody);
                break;
            case REQUEST_PATCH:
                if (mBodyBuilder == null) {
                    mBodyBuilder = new FormBody.Builder();
                }
                request.patch(requestBody);
                break;
            case REQUEST_DELETE:
                request.delete();
                break;
        }
    }

    private RequestBody checkBody() {
        if (mMultipartBulder != null) {
            if (mJsonObject != null) {
                mMultipartBulder.addPart(RequestBody.create(Tiny.JSON, mJsonObject.toString()));
            } else {
                return mMultipartBulder.build();
            }
        } else {
            if (mJsonObject != null) {
                return RequestBody.create(Tiny.JSON, mJsonObject.toString());
            }
        }
        return null;
    }

    public void enqueue(final TinyCallback callback) {
        Request.Builder request = new Request.Builder();
        request.url(mUrl);
        if (mHeaders != null) request.headers(mHeaders.build());
        handleBody(request);

        mClient.newCall(request.build()).enqueue(new Callback() {
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
        request.url(mUrl);
        if (mHeaders != null) request.headers(mHeaders.build());
        handleBody(request);
        try (Response response = mClient.newCall(request.build()).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}
