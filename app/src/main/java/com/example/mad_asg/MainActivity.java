package com.example.mad_asg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private TaskDatabase taskDatabase;

    String TITLE = "Task Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TITLE,"Created the page !");
    }

    @Override
    public void onStart(){
        super.onStart();
        // Initialize RecyclerView and its adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(adapter);

        // Initialize the database
        taskDatabase = new TaskDatabase(this);

        // Load tasks from the database
        taskList.addAll(taskDatabase.getAllTasks());
        adapter.notifyDataSetChanged();

        // Example button actions
        Button createButton = findViewById(R.id.createButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateTaskDialog();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = adapter.getSelectedPosition();
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    showDeleteConfirmationDialog(selectedPosition);
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        adapter.setSelectedPosition(position);
    }

    private void showCreateTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_create_task, null);

        final EditText taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
        final EditText taskDescEditText = dialogView.findViewById(R.id.taskDescriptionEditText);
        final EditText taskStartTimeEditText = dialogView.findViewById(R.id.taskStartTimeEditText);
        final EditText taskEndTimeEditText = dialogView.findViewById(R.id.taskEndTimeEditText);
        final EditText taskDurationEditText = dialogView.findViewById(R.id.taskDurationEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Task");
        builder.setView(dialogView);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskName = taskNameEditText.getText().toString().trim();
                String taskDesc = taskDescEditText.getText().toString().trim();
                String taskStartTime = taskStartTimeEditText.getText().toString().trim();
                String taskEndTime = taskEndTimeEditText.getText().toString().trim();
                String taskDurationString = taskDurationEditText.getText().toString().trim();

                // Validate user input
                boolean isValidInput = true;
                StringBuilder errorMessage = new StringBuilder("Invalid input. Please correct the following:");

                if (taskName.isEmpty()) {
                    isValidInput = false;
                    errorMessage.append("\n- Task name is required");
                }
                if (taskDesc.isEmpty()) {
                    isValidInput = false;
                    errorMessage.append("\n- Task description is required");
                }
                if (taskStartTime.isEmpty()) {
                    isValidInput = false;
                    errorMessage.append("\n- Task start time is required");
                } else {
                    if (!isValidTimeFormat(taskStartTime)) {
                        isValidInput = false;
                        errorMessage.append("\n- Task start time is in an invalid format");
                    }
                }
                if (taskEndTime.isEmpty()) {
                    isValidInput = false;
                    errorMessage.append("\n- Task end time is required");
                } else {
                    if (!isValidTimeFormat(taskEndTime)) {
                        isValidInput = false;
                        errorMessage.append("\n- Task end time is in an invalid format");
                    }
                }
                if (taskDurationString.isEmpty()) {
                    isValidInput = false;
                    errorMessage.append("\n- Task duration is required");
                } else {
                    try {
                        int taskDuration = Integer.parseInt(taskDurationString);
                        if (taskDuration <= 0) {
                            isValidInput = false;
                            errorMessage.append("\n- Task duration should be a positive number");
                        }
                    } catch (NumberFormatException e) {
                        isValidInput = false;
                        errorMessage.append("\n- Task duration should be a valid number");
                    }
                }

                if (isValidInput) {
                    // Create a new Task object
                    Task newTask = new Task(taskList.size() + 1, "Pending", taskName, taskDesc, new Date(),
                            taskStartTime, taskEndTime, Integer.parseInt(taskDurationString), "Type", "Repeat", 0, "", 1);

                    // Add the new task to the list and database
                    taskList.add(newTask);
                    taskDatabase.addTask(newTask);

                    // Notify the adapter of the new task
                    adapter.notifyItemInserted(taskList.size() - 1);
                } else {
                    // Display error message for invalid input
                    AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    errorDialogBuilder.setTitle("Invalid Input");
                    errorDialogBuilder.setMessage(errorMessage.toString());
                    errorDialogBuilder.setPositiveButton("OK", null);
                    errorDialogBuilder.create().show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private boolean isValidTimeFormat(String time) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            timeFormat.setLenient(false);
            timeFormat.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask(position);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void deleteTask(int position) {
        taskList.remove(position);
        adapter.setSelectedPosition(RecyclerView.NO_POSITION);
        adapter.notifyDataSetChanged();
    }
}

