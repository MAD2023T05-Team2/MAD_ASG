package com.example.mad_asg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DestressPage extends AppCompatActivity {

    Button vidFragButton, picFragButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destress_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_timer);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_timer) {
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(DestressPage.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(DestressPage.this, CalendarView.class));
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(DestressPage.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(DestressPage.this, MainActivity.class));
                return true;
            }
            return false;
        });

        picFragButton = findViewById(R.id.pictures);
        vidFragButton = findViewById(R.id.videos);

        replaceFragment(new DestressMessage());

        picFragButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    replaceFragment(new Pictures());

                }
            });


        vidFragButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    replaceFragment(new Videos());

                }
            });
        }


    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


}