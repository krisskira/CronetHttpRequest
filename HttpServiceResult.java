package com.example.elizabethgarcia.myapplication;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

public interface HttpServiceResult {
    void onSuccess(String data);
    void onSuccess(Bitmap bitmap);
    void onfail(@Nullable String data, int statusCode);
}
