package com.xindanxin.nutrilife.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.profile.Profile;

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

    public void openProfile(View v){
        EditText nombre = findViewById(R.id.userName);

        Intent intent = new Intent(Login.this, Profile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);


        intent.putExtra("user_name",nombre.getText().toString());

        startActivity(intent);
    }

    public void openRegister(View v){
        EditText nombre = findViewById(R.id.userName);

        Intent intent = new Intent(Login.this, SignUp.class);
        intent.putExtra("user_name",nombre.getText().toString());

        startActivity(intent);
    }
}