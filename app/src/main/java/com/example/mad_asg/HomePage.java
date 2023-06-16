package com.example.mad_asg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    final String TITLE = "Home Page";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Button Control
        Button timerButton = findViewById(R.id.TimerButton);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TITLE, "Timer Button clicked!");
            }
        });

        Button tasksButton = findViewById(R.id.TasksButton);
        tasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TITLE, "Task Button clicked!");
            }
        });

        Button calendarButton = findViewById(R.id.CalendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TITLE, "Calendar Button clicked!");
            }
        });

        tasksButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HomePage.this,MainActivity.class);
                        startActivity(i);
                    }
                }
        );
    }
}