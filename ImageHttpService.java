package com.example.elizabethgarcia.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageHttpService extends AsyncTask<String, Integer, Bitmap> {

    HttpServiceResult onResult;

    public ImageHttpService(String urlImage, HttpServiceResult onResult) {
        this.onResult = onResult;
        execute(new String[]{ urlImage });
    }

    private Bitmap getImageFromUrl(String url ){
        Bitmap b = null;

        try{
            URL aUrl = new URL(url);
            URLConnection connection = aUrl.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            b = BitmapFactory.decodeStream(bufferedInputStream);
            bufferedInputStream.close();
            inputStream.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return  b;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        return getImageFromUrl(strings [strings.length - 1]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        onResult.onSuccess(bitmap);
    }
}
