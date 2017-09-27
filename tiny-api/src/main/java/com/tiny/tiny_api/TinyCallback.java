package com.tiny.tiny_api;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by meo on 9/19/17.
 */

public interface TinyCallback {
    public void onSuccess(Response response);
    public void onFailure(Call call, IOException e);
    public void onResponse(Call call, Response response);
}
