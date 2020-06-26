package com.elton.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
Handler handler;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

    handler=new Handler();
        handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent intent=new Intent(SplashActivity.this,DiscoverActivity.class);
            startActivity(intent);
            finish();
        }
    },3000);

}
}