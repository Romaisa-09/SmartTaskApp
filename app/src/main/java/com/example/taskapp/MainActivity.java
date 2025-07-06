package com.example.taskapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.taskapp.fragment.AddTaskFragment;
import com.example.taskapp.fragment.ProfileFragment;
import com.example.taskapp.fragment.TasksListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_NOTIF = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1) Request POST_NOTIFICATIONS on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{ Manifest.permission.POST_NOTIFICATIONS },
                        REQ_NOTIF
                );
            }
        }

        // 2) Bottom nav setup
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setOnItemSelectedListener(item -> {
            Fragment f;
            int id = item.getItemId();
            if (id == R.id.nav_add) {
                f = new AddTaskFragment();
            } else if (id == R.id.nav_profile) {
                f = new ProfileFragment();
            } else {
                f = new TasksListFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, f)
                    .commit();
            return true;
        });

        // 3) Select "Tasks" by default
        if (savedInstanceState == null) {
            nav.setSelectedItemId(R.id.nav_tasks);
        }
    }

    // (Optional) handle the user’s response to the permission request:
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_NOTIF) {
            // You could check grantResults[0] here and show a toast if denied
        }
    }
}
