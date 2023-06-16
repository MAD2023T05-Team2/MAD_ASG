package com.example.mad_asg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter homeTaskadapter;
    private List<Task> homeTaskList;
    private TaskDatabase taskDatabase;
    final String TITLE = "Home Page";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    @Override
    protected void onStart(){
        super.onStart();
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
                Intent i = new Intent(HomePage.this,MainActivity.class);
                startActivity(i);
            }
        });

        Button calendarButton = findViewById(R.id.CalendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v(TITLE, "Calendar Button clicked!");
                Intent i = new Intent(HomePage.this, CalendarView.class);
                startActivity(i);
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