package com.finalproject3.amir.testgooglemaps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;

/**
 * Created by stlb54 on 03/05/2016.
 */
public class OpeningActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(OpeningActivity.this, MainActivity.class);
               OpeningActivity.this.startActivity(mainIntent);
              OpeningActivity.this.finish();
            }
        }, 5000);
    }
}
