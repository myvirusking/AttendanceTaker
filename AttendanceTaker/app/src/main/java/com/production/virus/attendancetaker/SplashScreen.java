package com.production.virus.attendancetaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    ImageView imgLogo;
    TextView txtSAS,txtSas;
    Animation splash_anim,splash_anim2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imgLogo = findViewById(R.id.imgLogo);
        txtSAS = findViewById(R.id.txtSAS);
        txtSas = findViewById(R.id.txtSas);
        splash_anim = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        splash_anim2 = AnimationUtils.loadAnimation(this,R.anim.splash_anim2);

        imgLogo.setAnimation(splash_anim);
        txtSAS.setAnimation(splash_anim2);
        txtSas.setAnimation(splash_anim2);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent in = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(in);
            }
        });

        thread.start();



    }
}
