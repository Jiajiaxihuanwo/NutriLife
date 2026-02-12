package com.xindanxin.nutrilife.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.xindanxin.nutrilife.R;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText email, password, password2, userName;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ImageView foton = findViewById(R.id.background);
        Glide.with(this)
                .load(R.drawable.b4)
                .centerCrop()
                .into(foton);

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);

    }

    public void Register(View v){
        crearCuenta();
    }

    public void Cancel(View v){
        startActivity(new Intent(SignUp.this, Login.class));
        finish();
    }

    private void crearCuenta(){
        String emailStr = email.getText().toString().trim();
        String passStr = password.getText().toString().trim();
        String pass2Str = password2.getText().toString().trim();

        if(emailStr.isEmpty() || passStr.isEmpty() || pass2Str.isEmpty()){
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!passStr.equals(pass2Str)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear usuario en Firebase Auth
        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if(firebaseUser != null){
                            String uid = firebaseUser.getUid();

                            // Guardar datos adicionales en Firestore
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("email", emailStr);
                            userMap.put("createdAt", System.currentTimeMillis());

                            db.collection("users").document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SignUp.this, "Cuenta creada y datos guardados", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUp.this, Login.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SignUp.this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(SignUp.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}