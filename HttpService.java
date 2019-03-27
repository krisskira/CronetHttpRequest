package com.example.elizabethgarcia.myapplication;

import android.content.Context;
import android.util.Log;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class HttpService extends UrlRequest.Callback {

    static HttpService shared = new HttpService();
    final String URL_API = "https://thesimpsonsquoteapi.glitch.me/quotes";
    final String TAG_LOG = "***-> HttpService";


    private Context context;
    private HttpServiceResult onResult;
    private CronetEngine.Builder builder;
    private CronetEngine cronetEngine;

    private void HttpService(){}

    public  HttpService setup(Context context, HttpServiceResult onResult){

        this.context = context;
        this.onResult = onResult;

        this.builder = new CronetEngine.Builder(context);
        this.cronetEngine = builder.build();

        return this;
    }

    void executeRequest() {
        Executor executor = Executors.newSingleThreadExecutor();
        UrlRequest.Builder urlRequestBuilder = cronetEngine.newUrlRequestBuilder(URL_API, this, executor);
        UrlRequest request = urlRequestBuilder.build();
        request.start();
    }

    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {

        ByteBuffer byteBuffer =  ByteBuffer.allocateDirect( 102400 );
        byteBuffer.clear();
        request.read( byteBuffer );
        // byte[] b =  byteBuffer.array();
        // Log.d(TAG_LOG, "Response Start: " + new String( b , "ASCII").trim() );
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) throws Exception {
        byteBuffer.clear();
        byte[] b = byteBuffer.array();
        String json = new String( b , "ASCII").trim();
        onResult.onSuccess(json);
        Log.d(TAG_LOG, "onReadCompleted: " + json );
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {

        try {

            ByteBuffer byteBuffer =  ByteBuffer.allocateDirect( 102400 );
            byteBuffer.clear();
            request.read( byteBuffer );

            byte[] b = byteBuffer.array();
            String json = new String( b , "ASCII").trim();
            // onResult.onSuccess(json);
            Log.d(TAG_LOG, "onSucceeded: " + json );

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {

        try {

            ByteBuffer byteBuffer =  ByteBuffer.allocateDirect( 102400 );
            byteBuffer.clear();
            request.read( byteBuffer );

            byte[] b = byteBuffer.array();
            String json = new String( b , "ASCII").trim();

            JSONObject data = new JSONObject();

            data.put("error_message", error.getMessage());
            data.put("error_locaized_message", error.getMessage());
            data.put("data", json);

            onResult.onfail(data.toString(), info.getHttpStatusCode());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG_LOG, "onFailed: " + String.valueOf( info.getHttpStatusCode()) );
    }
}
