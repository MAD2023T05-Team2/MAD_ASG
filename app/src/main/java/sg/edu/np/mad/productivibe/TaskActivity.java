package sg.edu.np.mad.productivibe;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

// The task page allows users to create, edit, delete and view all their tasks at a glance
public class TaskActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    public TaskAdapter adapter;
    private List<Task> taskList;
    private Database taskDatabase;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    String TITLE = "Task Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TITLE, "Main Activity");

        // Setting up bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_tasks);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_tasks) {
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(TaskActivity.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(TaskActivity.this, CalendarPage.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(TaskActivity.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_statistics) {
                startActivity(new Intent(TaskActivity.this, StatisticsPage.class));
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
        taskList = new ArrayList<>(); //holds data for tasks displayed in recycler view
        adapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(adapter);

        // Task divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Initialize the database
        taskDatabase = Database.getInstance(this);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserId", null);
        // Load tasks from the database
        taskList.addAll(taskDatabase.getAllTasksFromUser(userId));
        adapter.notifyDataSetChanged();

        // Implementation of swipe gesture for editing/deletion of tasks
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Floating Button Action
        FloatingActionButton createTask = findViewById(R.id.createTask);
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateTaskDialog();
            }
        });

        // To Calendar view button
        FloatingActionButton toCalendar = findViewById(R.id.CalendarButton);
        toCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskActivity.this, CalendarPage.class));
            }
        });

        // Search view
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.setTasks(taskList); // Show all tasks again when search is closed
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    // Swipe gesture functionality
    ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                // Swipe left to delete task
                showDeleteConfirmationDialog(position);
            } else if (direction == ItemTouchHelper.RIGHT) {
                // Swipe right to edit task
                showEditTaskDialog(position);
            }
        }
        // Decorating the swipe gesture with colors and icons so users know that they have swiped correctly
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.chart_circle_color))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.teal))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .addSwipeRightActionIcon(R.drawable.baseline_edit_calendar_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    // displays a dialog for creating a new task when create button is clicked
    private void showCreateTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_create_task, null);

        EditText taskNameEditText = dialogView.findViewById(R.id.taskNameEditText);
        EditText taskDescEditText = dialogView.findViewById(R.id.taskDescriptionEditText);
        EditText taskDurationEditText = dialogView.findViewById(R.id.taskDurationEditText);
        EditText taskDateTimeEditText = dialogView.findViewById(R.id.taskDateTimeEditText);
        EditText taskDueDateTimeEditText = dialogView.findViewById(R.id.taskDueDateTimeEditText);

        // Constructing the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Task");
        builder.setView(dialogView);

        builder.setPositiveButton("Create", null); // Set initially disabled
        builder.setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button createButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Validate user input
                        boolean isValidInput = validateInput(taskNameEditText, taskDescEditText, taskDateTimeEditText, taskDueDateTimeEditText, taskDurationEditText);
                        if (isValidInput) {
                            String taskName = taskNameEditText.getText().toString().trim();
                            String taskDesc = taskDescEditText.getText().toString().trim();
                            String taskDurationString = taskDurationEditText.getText().toString().trim();
                            String taskDateTime = taskDateTimeEditText.getText().toString().trim();
                            String taskDueDateTime = taskDueDateTimeEditText.getText().toString().trim();

                            // Convert the edited date strings to Date objects
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
                            Date taskDateTimed = null;
                            Date taskDueDateTimed = null;
                            try {
                                taskDateTimed = dateFormat.parse(taskDateTime);
                                taskDueDateTimed = dateFormat.parse(taskDueDateTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // Create a new Task object
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String userId = sharedPreferences.getString("UserId", null);
                            Task newTask = new Task(
                                    taskList.size() + 1, "Pending", taskName, taskDesc, taskDateTimed,
                                    taskDueDateTimed, Integer.parseInt(taskDurationString), "Type",
                                    "Repeat", 0, "", 1 // saves the userID
                            );

                            // Add the new task to the list and database
                            taskList.add(newTask);
                            taskDatabase.addTask(newTask);

                            // Notify the adapter of the new task
                            adapter.notifyItemInserted(taskList.size() - 1);

                            // Trigger Notification Scheduling
                            notificationChannel();
                            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(TaskActivity.this, BroadcastReceiver.class), PendingIntent.FLAG_IMMUTABLE);
                            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            sendPushNotification(newTask);

                            // Dismiss dialog
                            dialog.dismiss();
                        }
                    }
                });

                Button cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    // displays a dialog to edit a task when swiped right
    private void showEditTaskDialog(final int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_task, null);

        EditText taskNameEditText = dialogView.findViewById(R.id.editTaskNameEditText);
        EditText taskDescEditText = dialogView.findViewById(R.id.editTaskDescriptionEditText);
        EditText taskDurationEditText = dialogView.findViewById(R.id.editTaskDurationEditText);
        EditText taskDateTimeEditText = dialogView.findViewById(R.id.editTaskDateTimeEditText);
        EditText taskDueDateTimeEditText = dialogView.findViewById(R.id.editTaskDueDateTimeEditText);

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

        // Constructing the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");
        builder.setView(dialogView);

        builder.setPositiveButton("Save", null); // Set initially disabled
        builder.setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button createButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Validate user input
                        boolean isValidInput = validateInput(taskNameEditText, taskDescEditText, taskDateTimeEditText, taskDueDateTimeEditText, taskDurationEditText);
                        if (isValidInput) {
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
                        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(TaskActivity.this, BroadcastReceiver.class), PendingIntent.FLAG_IMMUTABLE);
                        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        // Cancel & Push Notification again
                        cancelNotification();
                        sendPushNotification(task);

                        //Dismiss dialog
                        dialog.dismiss();

                        }
                    }
                });

                Button cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

    // displays a dialog to confirm the deletion of a task when swiped left
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask(position); //delete task from database
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.notifyItemChanged(position); // When cancelled, position remains so it stays on the tasks page
            }
        });
        builder.create().show();
    }

    // actual deletion of the task from the list and the database
    private void deleteTask(int position) {
        Task task = taskList.get(position); // Retrieve the task to be deleted
        taskList.remove(position); // Remove the task from the list
        adapter.notifyItemRemoved(position); // Notify the adapter of item removal

        Database taskDatabase = Database.getInstance(this); // Get the TaskDatabase instance
        taskDatabase.deleteTask(task.getId()); // Delete the task from the database

        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent); // Halt the notifications for the deleted task
        }
        adapter.notifyDataSetChanged(); // Update the RecyclerView

        // Undoing the task deletion
        Snackbar.make(recyclerView, "Task deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskList.add(position, task);
                        adapter.notifyItemInserted(position);
                        recyclerView.scrollToPosition(position);
                    }
                }) .show();
    }

    private void performSearch(String query) {
        List<Task> filteredTasks = new ArrayList<>();
        if (query.isEmpty()) {
            // If the query is empty, show all tasks
            filteredTasks.addAll(taskList);
        } else {
            // Perform the search based on the query
            String lowerCaseQuery = query.toLowerCase();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());

            for (Task task : taskList) {
                String taskName = task.getTaskName().toLowerCase();
                String taskDesc = task.getTaskDesc().toLowerCase();
                String taskDateTime = dateFormat.format(task.getTaskDateTime()).toLowerCase();
                String taskDueDateTime = dateFormat.format(task.getTaskDueDateTime()).toLowerCase();
                String taskDuration = String.valueOf(task.getTaskDuration());

                if (taskName.contains(lowerCaseQuery) || taskDesc.contains(lowerCaseQuery) ||
                        taskDateTime.contains(lowerCaseQuery) || taskDueDateTime.contains(lowerCaseQuery) ||
                        taskDuration.contains(lowerCaseQuery)) {
                    filteredTasks.add(task);
                }
            }
        }
        adapter.setTasks(filteredTasks); // Update the adapter with filtered tasks
        adapter.notifyDataSetChanged();
    }

        public void sendPushNotification(Task task) {
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(task.getTaskDateTime()); // Set deadline based on user input
        Calendar currentTime = Calendar.getInstance(); // Retrieve current time
        long milliDiff = deadline.getTimeInMillis() - currentTime.getTimeInMillis();
        double days = milliDiff / (24 * 60 * 60 * 1000);
        int daysDiff = (int) Math.round(days); // Calculate days till deadline rounded to nearest whole

        if (daysDiff<=14) {
            int interval4days = 259200000; // Once every 3 days until it hits | Intervals are in milliseconds
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime.getTimeInMillis(), interval4days, pendingIntent);
        }
        else {
            int interval1week = 604800000; // Once every week | Intervals are in milliseconds
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime.getTimeInMillis(), interval1week, pendingIntent);
        }
    }

    public void notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("Task Reminder Notification", "Task Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    public void cancelNotification() {
        alarmManager.cancel(pendingIntent);
    }

    // ------------------------------------------------------- VALIDATION CODE ---------------------------------------------------------------------------

    private boolean validateInput(EditText taskNameEditText, EditText taskDescEditText, EditText taskDateTimeEditText, EditText taskDueDateTimeEditText, EditText taskDurationEditText) {
        boolean isValidInput = true;

        String taskName = taskNameEditText.getText().toString().trim();
        String taskDesc = taskDescEditText.getText().toString().trim();
        String taskDurationString = taskDurationEditText.getText().toString().trim();
        String taskDateTime = taskDateTimeEditText.getText().toString().trim();
        String taskDueDateTime = taskDueDateTimeEditText.getText().toString().trim();

        if (taskName.isEmpty()) {
            isValidInput = false;
            taskNameEditText.setError("Task name is required");
        }

        if (taskDesc.isEmpty()) {
            isValidInput = false;
            taskDescEditText.setError("Task description is required");
        }

        if (taskDateTime.isEmpty() || !isValidDateTimeFormat(taskDateTime)) {
            isValidInput = false;
            taskDateTimeEditText.setError("Invalid date and time format");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
            Date taskDateD = null;
            try {
                taskDateD = dateFormat.parse(taskDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (taskDateD != null && taskDateD.before(new Date())) {
                isValidInput = false;
                taskDateTimeEditText.setError("Task start date and time cannot be in the past");
            } else {
                taskDateTimeEditText.setError(null);
            }
        }

        if (taskDueDateTime.isEmpty() || !isValidDateTimeFormat(taskDueDateTime)) {
            isValidInput = false;
            taskDueDateTimeEditText.setError("Invalid due date and time format");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
            Date taskDueDateD = null;
            try {
                taskDueDateD = dateFormat.parse(taskDueDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (taskDueDateD != null && taskDueDateD.before(new Date())) {
                isValidInput = false;
                taskDueDateTimeEditText.setError("Task due date and time cannot be in the past");
            } else {
                taskDueDateTimeEditText.setError(null);
            }
        }

        if (taskDurationString.isEmpty()) {
            isValidInput = false;
            taskDurationEditText.setError("Task duration is required");
        } else {
            try {
                int taskDuration = Integer.parseInt(taskDurationString);
                if (taskDuration <= 0) {
                    isValidInput = false;
                    taskDurationEditText.setError("Duration must be a positive number");
                } else {
                    taskDurationEditText.setError(null);
                }
            } catch (NumberFormatException e) {
                isValidInput = false;
                taskDurationEditText.setError("Duration should be a valid number");
            }
        }
        return isValidInput;
    }

    private boolean isValidDateTimeFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }}




