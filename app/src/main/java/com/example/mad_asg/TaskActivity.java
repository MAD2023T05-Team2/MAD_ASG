package com.example.mad_asg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity implements TaskAdapter.TaskAdapterListener{
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private Button launchTaskButton;
    private static final String TAG = "MainActivity";
    private List<Task> taskList; // Declare the taskList variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the taskList variable
        taskList = new ArrayList<>();

        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        launchTaskButton = findViewById(R.id.launchTaskButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskRecyclerView.setLayoutManager(layoutManager);

        taskAdapter = new TaskAdapter(taskList, this, this); // Pass the listener (this)
        taskRecyclerView.setAdapter(taskAdapter);
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
        taskList = getTasksFromDatabase(); // Replace this with your database or data source logic

        if (taskList.isEmpty()) {
            // No tasks, display a message
            Toast.makeText(this, "No tasks today!", Toast.LENGTH_SHORT).show();
        }

        taskAdapter.setTaskList(taskList);
        taskAdapter.notifyDataSetChanged();
    }

    private List<Task> getTasksFromDatabase() {
        List<Task> taskList = new ArrayList<>();

        // Create some sample tasks
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            // Parse the date string to a Date object
            Date task1Date = dateFormat.parse("2023-06-10");

            // Create a sample task using the Date object
            Task task1 = new Task(1, "Not Done", "Task 1", "Description 1", task1Date,
                    "09:00 AM", "10:00 AM", 60, "Yes",
                    "2023-06-10 08:45 AM", "Type 1", "Yes", 0,
                    "Every 2 days", 1);

            taskList.add(task1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            // Parse the date string to a Date object
            Date task2Date = dateFormat.parse("2023-06-10");

            // Create a sample task using the Date object
            Task task2 = new Task(2, "Not Done", "Task 2", "Description 2", task2Date,
                    "02:00 PM", "04:00 PM", 120, "Yes",
                    "2023-06-11 01:45 PM", "Type 2", "Yes", 0,
                    "Every Week", 1);

            taskList.add(task2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return taskList;
    }


    @Override
    public void onTaskEdit(Task task) {
        // Handle task edit operation
        // you can launch an activity to edit the task
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra("task", task);
        startActivity(intent);
    }

    @Override
    public void onTaskDelete(Task task) {
        // Handle task delete operation
        // For example, you can show a confirmation dialog and delete the task if confirmed
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the task
                deleteTask(task);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteTask(Task task) {
        // Perform the deletion operation here
        // You can remove the task from the taskList and notify the adapter

        // Example implementation:
        taskList.remove(task); // Remove the task from the list
        taskAdapter.notifyDataSetChanged(); // Notify the adapter about the data change
    }


    public void onEditButtonClick(View view) {
        // Retrieve the task associated with the clicked editButton
        Task task = (Task) view.getTag();

        // Handle the edit operation for the task
        // You can launch an activity to edit the task or show a dialog, etc.
        onTaskEdit(task);
    }

    public void onDeleteButtonClick(View view) {
        // Retrieve the task associated with the clicked deleteButton
        Task task = (Task) view.getTag();

        // Handle the delete operation for the task
        // You can show a confirmation dialog and delete the task if confirmed, etc.
        onTaskDelete(task);
    }


}
