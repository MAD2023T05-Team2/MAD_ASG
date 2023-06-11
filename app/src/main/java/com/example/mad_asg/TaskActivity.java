package com.example.mad_asg;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private Button launchTaskButton;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        launchTaskButton = findViewById(R.id.launchTaskButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskRecyclerView.setLayoutManager(layoutManager);
        // Set click listener for the launchTaskButton
        launchTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskRecyclerView.setVisibility(View.VISIBLE);
                populateTaskRecyclerView();
            }
        });

        // Populate the RecyclerView with data
        populateTaskRecyclerView();
    }

    private void populateTaskRecyclerView() {
        List<Task> taskList = new ArrayList<>();

        // Create some sample tasks
        Task task1 = new Task(1, "Not Done", "Task 1", "Description 1", "2023-06-10",
                "09:00 AM", "10:00 AM", 60, "Yes",
                "2023-06-10 08:45 AM", "Type 1", "Yes", 0,
                "Every 2 days", 1);

        Task task2 = new Task(2, "Not Done", "Task 2", "Description 2", "2023-06-11",
                "02:00 PM", "04:00 PM", 120, "Yes",
                "2023-06-11 01:45 PM", "Type 2", "Yes", 0,
                "Every Week", 1);

        taskList.add(task1);
        taskList.add(task2);

        taskAdapter = new TaskAdapter(taskList, this);
        taskRecyclerView.setAdapter(taskAdapter);

        Log.d(TAG, "Task List: " + taskList.toString());
        Log.d(TAG, "Task Adapter: " + taskAdapter.toString());
    }
}
