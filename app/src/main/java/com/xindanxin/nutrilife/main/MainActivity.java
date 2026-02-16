package com.xindanxin.nutrilife.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.xindanxin.nutrilife.R;
import com.xindanxin.nutrilife.auth.Login;
import com.xindanxin.nutrilife.dashboard.Dashboard;
import com.xindanxin.nutrilife.goal.goal;
import com.xindanxin.nutrilife.meals.Meals;
import com.xindanxin.nutrilife.profile.Profile;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, Login.class));
            finish();
            return;
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = new Dashboard();

            if (item.getItemId() == R.id.nav_dashboard) {
                fragment = new Dashboard();
            } else if (item.getItemId() == R.id.nav_meals) {
                fragment = new Meals();
            } else if (item.getItemId() == R.id.nav_calendar) {
//                fragment = new Planner();
            } else if (item.getItemId() == R.id.nav_goals) {
                fragment = new goal();
            } else if (item.getItemId() == R.id.nav_profile) {
                 fragment = new Profile();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            return true;
        });

        bottomNav.setSelectedItemId(R.id.nav_dashboard);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // No hay sesión → mandar a Login
            startActivity(new Intent(this, Login.class));
            finish();
        }
    }
}