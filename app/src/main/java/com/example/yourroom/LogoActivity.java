package com.example.yourroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LogoActivity extends Activity {
    private Animation logoAnim, buttonLogoAnim;
    // private Button bAnim;
    private ImageView logoImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        init();
        startMainActivity();
    }
    private void init(){
        logoAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.logo_anim);
        //buttonLogoAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_anim);
        logoImage = findViewById(R.id.logoView);
        //bAnim = findViewById(R.id.button);
        logoImage.startAnimation(logoAnim);
        //bAnim.startAnimation(buttonLogoAnim);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
    private void startMainActivity(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(LogoActivity.this, LoginActivity.class);
                startActivity(i);
            }
        }).start();
    }
}
