package com.example.mad_asg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter homeTaskadapter;
    private List<Task> homeTaskList;
    private TaskDatabase taskDatabase;
    final String TITLE = "Home Page";
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(HomePage.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(HomePage.this, CalendarPage.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(HomePage.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(HomePage.this, MainActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
/*
        ImageView home = findViewById(R.id.homeicon);
        home.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                Log.v(TITLE, "Task Button clicked!");
                Intent i = new Intent(HomePage.this, HomePage.class);
                startActivity(i);
                return false;
            }
        });

        ImageView tasks = findViewById(R.id.todolist);
        tasks.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                Log.v(TITLE, "Task Button clicked!");
                Intent i = new Intent(HomePage.this, MainActivity.class);
                startActivity(i);
                return false;
            }
        });

        ImageView calendar = findViewById(R.id.calendaricon);
        calendar.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                Log.v(TITLE, "Task Button clicked!");
                Intent i = new Intent(HomePage.this, CalendarView.class);
                startActivity(i);
                return false;
            }
        });

        ImageView timer = findViewById(R.id.timericon);
        timer.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                Log.v(TITLE, "Task Button clicked!");
                Intent i = new Intent(HomePage.this, MainActivity.class);
                startActivity(i);
                return false;
            }
        });
        /*for (int i = 0; i <100; i++){
            Task myData = new Task();
            myData.setMyImageID(R.drawable.spongebob);
            myData.setMyText(String.valueOf(i));
            myList.add(myData);
        }
        // Initialize RecyclerView and its adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeTaskList = new ArrayList<>();
        homeTaskadapter = new TaskAdapter(homeTaskList, this);
        recyclerView.setAdapter(homeTaskadapter);

        // Initialize the database
        taskDatabase = new TaskDatabase(this);

        // Load tasks from the database
        List<Task> allTasks = taskDatabase.getAllTasks();
        homeTaskadapter.notifyDataSetChanged();
*/
    }

}