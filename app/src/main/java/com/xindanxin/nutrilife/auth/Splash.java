package com.xindanxin.nutrilife.auth;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.xindanxin.nutrilife.R;


public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView foton = findViewById(R.id.background);
        Glide.with(this)
                .load(R.drawable.b1)
                .centerCrop()
                .into(foton);
        openApp();


        CardView cardLogo = findViewById(R.id.card_logo);
        Animation imgAnim = AnimationUtils.loadAnimation(this,R.anim.anim1);
        cardLogo.startAnimation(imgAnim);

        TextView text = findViewById(R.id.app_name);
        Animation textAnim = AnimationUtils.loadAnimation(this,R.anim.anim2);
        text.startAnimation(textAnim);

    }
    private void openApp(){
        Handler handler = new Handler();
        handler.postDelayed(()-> {
            Intent intent = new Intent(Splash.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        },2000);
    }
}