package com.example.elizabethgarcia.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, HttpServiceResult {

    ImageView image;
    TextView character_name, quote;
    Button btn_next;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.image_quote);
        character_name = (TextView) findViewById(R.id.character);
        quote = (TextView) findViewById(R.id.quote);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle( getString(R.string.app_name) );
        progressDialog.setCancelable(false);
        progressDialog.setMessage( getString(R.string.msg_please_wait) );

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.INTERNET
        }, 87);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                btn_next_OnClick();
                break;
        }
    }

    private void btn_next_OnClick() {
        progressDialog.show();
        HttpService.shared.setup(this, this).executeRequest();
    }

    @Override
    public void onSuccess(String data) {
        progressDialog.dismiss();

        try {

            JSONArray jsonArray = new JSONArray( data );
            JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);

            character_name.setText( jsonObject.get("character").toString() );
            quote.setText( jsonObject.get("quote").toString() );

            String urlImage = jsonObject.get("image").toString();
            new ImageHttpService(urlImage, this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Bitmap bitmap) {
        image.setImageBitmap( bitmap );
    }

    @Override
    public void onfail(@Nullable String data, int statusCode) {
        progressDialog.dismiss();
    }

}
