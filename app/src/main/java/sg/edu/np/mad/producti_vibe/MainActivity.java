package sg.edu.np.mad.producti_vibe;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListener, TaskAdapter.OnEditClickListener {
    private RecyclerView recyclerView;
    public TaskAdapter adapter;
    private List<Task> taskList;
    private TaskDatabase taskDatabase;
    public static final String SHARED_PREFS = "sharedPrefs";
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    String TITLE = "Task Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TITLE, "Main Activity");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_tasks);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_tasks) {
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(MainActivity.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(MainActivity.this, CalendarPage.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(MainActivity.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                return true;
            }
            return false;
        });
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
        taskDatabase = TaskDatabase.getInstance(this);


        // Load tasks from the database
        taskList.addAll(taskDatabase.getAllTasks());
        adapter.notifyDataSetChanged();

        //  button actions
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
        final EditText taskDurationEditText = dialogView.findViewById(R.id.taskDurationEditText);
        final EditText taskDateTimeEditText = dialogView.findViewById(R.id.taskDateTimeEditText);
        final EditText taskDueDateTimeEditText = dialogView.findViewById(R.id.taskDueDateTimeEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Task");
        builder.setView(dialogView);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskName = taskNameEditText.getText().toString().trim();
                String taskDesc = taskDescEditText.getText().toString().trim();
                String taskDurationString = taskDurationEditText.getText().toString().trim();
                String taskDateTime = taskDateTimeEditText.getText().toString().trim();
                String taskDueDateTime = taskDueDateTimeEditText.getText().toString().trim();

                // Convert the edited date strings to Date objects
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
                Date taskDateD = null;
                Date taskDueDateD = null;
                try {
                    taskDateD = dateFormat.parse(taskDateTime);
                    taskDueDateD = dateFormat.parse(taskDueDateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Validate user input
                if (validateInput(taskName, taskDesc, taskDateD, taskDueDateD, taskDurationString)) {
                    // Create a new Task object
                    Task newTask = new Task(
                            taskList.size() + 1, "Pending", taskName, taskDesc, taskDateD,
                            taskDueDateD, Integer.parseInt(taskDurationString), "Type",
                            "Repeat", 0, "", 1
                    );

                    // Add the new task to the list and database
                    taskList.add(newTask);
                    taskDatabase.addTask(newTask);

                    // Notify the adapter of the new task
                    adapter.notifyItemInserted(taskList.size() - 1);

                    // Trigger Notification Scheduling
                    notificationChannel();
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(MainActivity.this, BroadcastReceiver.class), PendingIntent.FLAG_IMMUTABLE);
                    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
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
        final EditText taskDurationEditText = dialogView.findViewById(R.id.editTaskDurationEditText);
        final EditText taskDateTimeEditText = dialogView.findViewById(R.id.editTaskDateTimeEditText);
        final EditText taskDueDateTimeEditText = dialogView.findViewById(R.id.editTaskDueDateTimeEditText);

        // Populate the EditText fields with the existing task data
        Task task = taskList.get(position);
        taskNameEditText.setText(task.getTaskName());
        taskDescEditText.setText(task.getTaskDesc());
        taskDurationEditText.setText(String.valueOf(task.getTaskDuration()));
        // Convert the date objects to string representations
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        String taskDateTime = dateFormat.format(task.getTaskDateTime());
        String taskDueDateTime = dateFormat.format(task.getTaskDueDateTime());

        taskDateTimeEditText.setText(taskDateTime);
        taskDueDateTimeEditText.setText(taskDueDateTime);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");
        builder.setView(dialogView);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the edited values from the EditText fields
                String editedTaskName = taskNameEditText.getText().toString().trim();
                String editedTaskDesc = taskDescEditText.getText().toString().trim();
                int editedTaskDuration = Integer.parseInt(taskDurationEditText.getText().toString().trim());
                String editedTaskDate = taskDateTimeEditText.getText().toString().trim();
                String editedTaskDueDate = taskDueDateTimeEditText.getText().toString().trim();

                // Convert the edited date strings to Date objects
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
                Date editedDate = null;
                Date editedDueDate = null;
                try {
                    editedDate = dateFormat.parse(editedTaskDate);
                    editedDueDate = dateFormat.parse(editedTaskDueDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Update the task in the list and database
                Task task = taskList.get(position);
                task.setTaskName(editedTaskName);
                task.setTaskDesc(editedTaskDesc);
                task.setTaskDuration(editedTaskDuration);
                task.setTaskDateTime(editedDate);
                task.setTaskDueDateTime(editedDueDate);

                taskDatabase.updateTask(task);

                // Notify the adapter of the updated task
                adapter.notifyItemChanged(position);

                // Trigger Notification Scheduling
                notificationChannel();
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(MainActivity.this, BroadcastReceiver.class), PendingIntent.FLAG_IMMUTABLE);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                // Cancel & Push Notification again
                cancelNotification();
                sendPushNotification(task);
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
        Task task = taskList.get(position); // Retrieve the task to be deleted
        taskList.remove(position); // Remove the task from the list
        adapter.notifyItemRemoved(position); // Notify the adapter of item removal

        TaskDatabase taskDatabase = TaskDatabase.getInstance(this); // Get the TaskDatabase instance
        taskDatabase.deleteTask(task.getId()); // Delete the task from the database

        adapter.notifyDataSetChanged(); // Update the RecyclerView
        alarmManager.cancel(pendingIntent); // Halt all notifications
    }

    public void sendPushNotification(Task task) {
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(task.getTaskDateTime()); // Set deadline based on user input
        Calendar currentTime = Calendar.getInstance(); // Retrieve current time
        long milliDiff = deadline.getTimeInMillis() - currentTime.getTimeInMillis();
        double days = milliDiff / (24 * 60 * 60 * 1000);
        int daysDiff = (int) Math.round(days); // Calculate days till deadline

        if (daysDiff<=14) {
            int interval4days = 30000; // Once every 4 days until it hits 345600000
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime.getTimeInMillis(), interval4days, pendingIntent);
        }
        else {
            int interval1week = 604800000; // Once every week
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime.getTimeInMillis(), interval1week, pendingIntent);
        }
    }

    public void notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("Task Deadline Notification", "Task Deadline", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    public void cancelNotification() {
        alarmManager.cancel(pendingIntent);
    }

    // ------------------------------------------------------- VALIDATION CODE ---------------------------------------------------------------------------

    private boolean validateInput(String taskName, String taskDesc, Date taskDateTime, Date taskDueDateTime, String taskDurationString) {
        boolean isValidInput = true;
        StringBuilder errorMessage = new StringBuilder("Invalid input. Please correct the following:");

        // check if empty or not
        if (taskName.isEmpty()) {
            isValidInput = false;
            errorMessage.append("\n- Task name is required");
        }
        if (taskDesc.isEmpty()) {
            isValidInput = false;
            errorMessage.append("\n- Task description is required");
        }

        // check to ensure not null and is in a valid datetime format
        // also checks if date is in the past
        if (taskDateTime == null || !isValidDateTimeFormat(taskDateTime)) {
            isValidInput = false;
            errorMessage.append("\n- Task start date and time are in an invalid format (dd/MM/yy HH:mm)");
        } else {
            if (taskDateTime.before(new Date())) {
                isValidInput = false;
                errorMessage.append("\n- Task start date and time cannot be in the past");
            }
        }

        if (taskDueDateTime == null || !isValidDateTimeFormat(taskDueDateTime)) {
            isValidInput = false;
            errorMessage.append("\n- Task due date and time are in an invalid format (dd/MM/yy HH:mm)");
        } else {
            if (taskDueDateTime.before(taskDateTime)) {
                isValidInput = false;
                errorMessage.append("\n- Task due date and time cannot be earlier than the start date and time");
            }
        }

        // check if not not null and not a integer
        // negative values are parsed into positive numbers
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

    private boolean isValidDateTimeFormat(Date date) {
        // Define the desired date and time format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());

        // Format the date object to a string in the desired format
        String dateString = dateFormat.format(date);

        // Parse the formatted string back to a date object
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            return false;
        }

        // Check if the parsed date object matches the original date object
        return parsedDate.equals(date);
    }


}



