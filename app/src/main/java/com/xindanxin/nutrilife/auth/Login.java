package com.xindanxin.nutrilife.auth;

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
import com.xindanxin.nutrilife.main.MainActivity;

import android.content.Intent;

public class Login extends AppCompatActivity {

    private EditText email, password;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ImageView foton = findViewById(R.id.background);
        Glide.with(this)
                .load(R.drawable.b4)
                .centerCrop()
                .into(foton);

        email = findViewById(R.id.userEmail); // cambia si tu EditText tiene otro ID
        password = findViewById(R.id.password);
    }

    // Botón login
    public void openProfile(android.view.View v){
        String emailStr = email.getText().toString().trim();
        String passStr = password.getText().toString().trim();

        if(emailStr.isEmpty() || passStr.isEmpty()){
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Login con Firebase Auth
        mAuth.signInWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if(firebaseUser != null){
                            String uid = firebaseUser.getUid();

                            // Traer datos del usuario de Firestore
                            db.collection("users").document(uid).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if(documentSnapshot.exists()){
                                            String emailDB = documentSnapshot.getString("email");
                                            String username = documentSnapshot.getString("username"); // opcional

                                            // Pasar datos a MainActivity
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            intent.putExtra("uid", uid);
                                            intent.putExtra("email", emailDB);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Login.this, "Usuario no encontrado en Firestore", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(Login.this, "Error al obtener datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Botón ir a registro
    public void openRegister(android.view.View v){
        startActivity(new Intent(Login.this, SignUp.class));
    }

}