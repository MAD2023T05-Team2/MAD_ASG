package sg.edu.np.mad.productivibe_;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskTimerPage extends AppCompatActivity implements TaskTimerListener {
    private static int TIMER_LENGTH = 30;
    private boolean isPaused = false;
    private boolean isRunning = false;
    private TaskTimerView mTimerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_timer_page);

        // Find the "Select Task" button in the layout file
        // and set an onClickListener for it
        Button selectTaskButton = findViewById(R.id.button);
        selectTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to show the dialog when the button is clicked
                showTaskSelectionDialog();
            }
        });

        // Setting the navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_timer);

        // When an item in the bottom navigation view is selected,
        // the code checks the item's ID and performs the corresponding action
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_timer) {
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(TaskTimerPage.this, TaskActivity.class));
                return true;
            } else if (itemId == R.id.bottom_destress) {
                startActivity(new Intent(TaskTimerPage.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(TaskTimerPage.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_statistics) {
                startActivity(new Intent(TaskTimerPage.this, StatisticsPage.class));
                return true;
            }
            return false;
        });


        // The TaskTimerListener interface is set to the activity,
        // allowing the TaskTimerView to communicate back to the TaskTimerPage activity
        mTimerView = (TaskTimerView) findViewById(R.id.timer);
        mTimerView.setTaskTimerListener(this);

        // Two buttons, timerStartButton and timerResetButton, are initialized
        final Button timerStartButton = (Button) findViewById(R.id.btn_timer_start);
        final Button timerResetButton = (Button) findViewById(R.id.btn_timer_reset);
        timerStartButton.setOnClickListener(new View.OnClickListener() {

            // the code checks if the timer is already running,
            // handles the case accordingly
            @Override
            public void onClick(View v) {
                // If the timer is paused (isPaused is true),
                // the button text is changed to "Pause," and the timer is resumed using mTimerView.resume()
                if(isRunning){
                    if(isPaused){
                        timerStartButton.setText("Pause");
                        mTimerView.resume();
                    }
                    // If the timer is not paused, the button text is changed to "Resume,"
                    // and the timer is paused using mTimerView.pause()
                    else {
                        timerStartButton.setText("Resume");
                        mTimerView.pause();
                    }
                    isPaused = !isPaused;
                }
                // If the timer is not running, the code starts the timer with a predefined
                // TIMER_LENGTH and changes the button text to "Resume."
                else{
                    mTimerView.start(TIMER_LENGTH);
                    isRunning = true;
                    timerStartButton.setText("Resume");
                }
            }


        });
        timerResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // The code stops the timer using mTimerView.stop(), sets isRunning to false,
            // and changes the timerStartButton text to "Start."
            public void onClick(View v) {
                mTimerView.stop();
                isRunning = false;
                timerStartButton.setText("Start");
                final TextView countDownText = (TextView) findViewById(R.id.countdown);
                // Reset the countdown text to its original value
                long originalDurationSeconds = 0;
                long originalMinute = originalDurationSeconds / 60;
                long originalSeconds = originalDurationSeconds % 60;
                String originalMinuteStr = ((originalMinute < 10) ? "0" : "") + originalMinute;
                String originalSecondsStr = ((originalSeconds < 10) ? "0" : "") + originalSeconds;
                countDownText.setText(originalMinuteStr + ":" + originalSecondsStr);
            }
        });
    }

    // Method to show the task selection dialog
    private void showTaskSelectionDialog() {
        // Initialize the database
        Database taskDatabase = Database.getInstance(this);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserId", null);

        // Call the getAllTasksFromUser method to retrieve tasks for the specified user ID.
        List<Task> taskList = taskDatabase.getAllTasksFromUser(userId);

        // Load tasks from the database
        taskList.addAll(taskDatabase.getAllTasksFromUser(userId));

        // Create a list of task names to display in the dialog
        ArrayList<String> taskNames = new ArrayList<>();
        for (Task task : taskList) {
            taskNames.add(task.getTaskName());
        }

        // Convert the ArrayList to a simple array
        String[] taskNamesArray = taskNames.toArray(new String[0]);

        // Create an AlertDialog with the list of task names
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Task");
        builder.setItems(taskNamesArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When a task is selected, update the timer duration with the selected task's duration
                if (which >= 0 && which < taskList.size()) {
                    Task selectedTask = taskList.get(which);
                    long taskDuration = selectedTask.getTaskDuration();
                    updateTimerDuration((int) taskDuration);
                }
            }
        });

        // Show the dialog
        builder.show();
    }
    // Method to update the timer duration
    private void updateTimerDuration(int duration) {
        // Update the TIMER_LENGTH with the selected task's duration
        TIMER_LENGTH = duration;

        // Update the TextView to display the new timer duration
        final TextView countDownText = findViewById(R.id.countdown);
        long minute = TIMER_LENGTH / 60;
        long seconds = TIMER_LENGTH % 60;
        String minuteStr = ((minute < 10) ? "0" : "") + minute;
        String secondsStr = ((seconds < 10) ? "0" : "") + seconds;

        if (countDownText != null) {
            countDownText.setText(minuteStr + ":" + secondsStr);
        }
    }

    @Override
    // the remaining seconds are converted to minutes and seconds format,
    // then displayed in the countDownText TextView
    public void onTaskTimerUpdate(long seconds){
        final TextView countDownText = (TextView) findViewById(R.id.countdown);
        long minute = seconds/60;
        seconds = seconds%60;
        String minuteStr = ((minute < 10)?"0":"") + minute;
        String secondsStr = ((seconds < 10)?"0":"") + seconds;

        if(countDownText != null){
            countDownText.setText(minuteStr+":"+secondsStr);
        }
    }
    @Override
    // overridden to stop the timer (mTimerView.stop())
    // when the activity goes into the paused stat
    protected void onPause() {
        mTimerView.stop();
        super.onPause();
    }
}