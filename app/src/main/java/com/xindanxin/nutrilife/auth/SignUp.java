package com.xindanxin.nutrilife.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.xindanxin.nutrilife.R;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ImageView foton = findViewById(R.id.background);
        Glide.with(this)
                .load(R.drawable.b4)
                .centerCrop()
                .into(foton);
    }

    public void Register(View v){
        crearCuenta();

        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
    }

    public void Cancel(View v){
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
    }

    private void crearCuenta(){}
}