package com.example.mad_asg;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListener, TaskAdapter.OnEditClickListener {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private TaskDatabase taskDatabase;

    String TITLE = "Task Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TITLE, "Main Activity");

    }

    @Override
    public void onStart() {
        super.onStart();
        // Initialize RecyclerView and its adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList, this, this);
        adapter.setOnItemClickListener(this); // Set item click listener
        adapter.setOnEditClickListener(this); // Set edit click listener
        recyclerView.setAdapter(adapter);

        // Initialize the database
        taskDatabase = new TaskDatabase(this);


        // Load tasks from the database
        taskList.addAll(taskDatabase.getAllTasks());
        adapter.notifyDataSetChanged();

        // Example button actions
        Button createButton = findViewById(R.id.createButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button editButton = findViewById(R.id.editButton);

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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = adapter.getSelectedPosition();
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    showEditTaskDialog(selectedPosition);
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        adapter.setSelectedPosition(position);
    }

    @Override
    public void onEditClick(int position) {
        showEditTaskDialog(position);
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
                if (validateInput(taskName, taskDesc, taskStartTime, taskEndTime, taskDurationString)) {
                    // Create a new Task object
                    Task newTask = new Task(
                            taskList.size() + 1, "Pending", taskName, taskDesc, new Date(),
                            taskStartTime, taskEndTime, Integer.parseInt(taskDurationString), "Type",
                            "Repeat", 0, "", 1, new Date()
                    );

                    // Add the new task to the list and database
                    taskList.add(newTask);
                    taskDatabase.addTask(newTask);

                    // Notify the adapter of the new task
                    adapter.notifyItemInserted(taskList.size() - 1);

                    // Trigger Task Notifications
                    sendPushNotification(newTask);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }


    private void showEditTaskDialog(final int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_task, null);

        final EditText taskNameEditText = dialogView.findViewById(R.id.editTaskNameEditText);
        final EditText taskDescEditText = dialogView.findViewById(R.id.editTaskDescriptionEditText);
        final EditText taskStartTimeEditText = dialogView.findViewById(R.id.editTaskStartTimeEditText);
        final EditText taskEndTimeEditText = dialogView.findViewById(R.id.editTaskEndTimeEditText);
        final EditText taskDurationEditText = dialogView.findViewById(R.id.editTaskDurationEditText);

        // Populate the EditText fields with the existing task data
        Task task = taskList.get(position);
        taskNameEditText.setText(task.getTaskName());
        taskDescEditText.setText(task.getTaskDesc());
        taskStartTimeEditText.setText(task.getTaskStartTime());
        taskEndTimeEditText.setText(task.getTaskEndTime());
        taskDurationEditText.setText(String.valueOf(task.getTaskDuration()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");
        builder.setView(dialogView);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the edited values from the EditText fields
                String editedTaskName = taskNameEditText.getText().toString().trim();
                String editedTaskDesc = taskDescEditText.getText().toString().trim();
                String editedTaskStartTime = taskStartTimeEditText.getText().toString().trim();
                String editedTaskEndTime = taskEndTimeEditText.getText().toString().trim();
                int editedTaskDuration = Integer.parseInt(taskDurationEditText.getText().toString().trim());

                // Update the task in the list and database
                Task task = taskList.get(position);
                task.setTaskName(editedTaskName);
                task.setTaskDesc(editedTaskDesc);
                task.setTaskStartTime(editedTaskStartTime);
                task.setTaskStartTime(editedTaskEndTime);
                task.setTaskDuration(editedTaskDuration);

                taskDatabase.updateTask(task);

                // Notify the adapter of the updated task
                adapter.notifyItemChanged(position);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
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


    // ------------------------------------------------------- VALIDATION CODE ---------------------------------------------------------------------------
    private boolean validateInput(String taskName, String taskDesc, String taskStartTime, String taskEndTime, String taskDurationString) {
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

        if (!isValidInput) {
            // Display error message for invalid input
            AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            errorDialogBuilder.setTitle("Invalid Input");
            errorDialogBuilder.setMessage(errorMessage.toString());
            errorDialogBuilder.setPositiveButton("OK", null);
            errorDialogBuilder.create().show();
        }

        return isValidInput;
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

    public void sendPushNotification(Task task) {
        // Notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("Task Deadline Notification", "Task Deadline", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

            String taskDeadline = task.getTaskEndTime(); // To be edited after date is added
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                Date deadline = sdf.parse(taskDeadline);
                Date currentTime = new Date();
                long timeRemaining = deadline.getTime() - currentTime.getTime();

                // Notification sent if the time remaining is <= 2 hours
                if (timeRemaining <= 7200000) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // Calculating the time left
                    long hour = timeRemaining / 3600000;
                    long minutes = (timeRemaining % 3600000) / 60000;
                    NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.this, "Task Deadline Notification")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("'" + task.getTaskName() + "' Deadline Approaching!")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Your task ending in " + hour + " hour " + minutes + " minutes, don't miss the deadline! Complete your task before the deadline hits."))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent);

                    // Notification Scheduling
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    //long triggerTime = deadline; // When the alarm goes off
                    //int interval = 120000; //1800000; // Alarm to repeat every 30 minutes
                    //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval, pendingIntent);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    notificationManager.notify(1, notification.build());
                }
            } catch (ParseException e) {
                e.printStackTrace(); }
        }
    }
}

