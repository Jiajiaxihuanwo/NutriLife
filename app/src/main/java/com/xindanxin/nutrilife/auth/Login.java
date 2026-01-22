package com.xindanxin.nutrilife.auth;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.xindanxin.nutrilife.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ImageView foton = findViewById(R.id.background);
        Glide.with(this)
                .load(R.drawable.b4)
                .centerCrop()
                .into(foton);
    }
}