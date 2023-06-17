package com.example.mad_asg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CalendarView extends AppCompatActivity {

    String TITLE = "Calendar View";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Log.v(TITLE,"Created the page !");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_calendar);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_calendar) {
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(CalendarView.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(CalendarView.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(CalendarView.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(CalendarView.this, MainActivity.class));
                return true;
            }
            return false;
        });
    }

}
