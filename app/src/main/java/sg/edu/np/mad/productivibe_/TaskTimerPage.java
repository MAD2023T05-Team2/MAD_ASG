package sg.edu.np.mad.productivibe_;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TaskTimerPage extends AppCompatActivity implements TaskTimerListener {
    private static final int TIMER_LENGTH = 30;
    private boolean isPaused = false;
    private boolean isRunning = false;
    private TaskTimerView mTimerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_timer_page);

        // Setting the navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_timer);

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

        mTimerView = (TaskTimerView) findViewById(R.id.timer);
        mTimerView.setTaskTimerListener(this);
        final Button timerStartButton = (Button) findViewById(R.id.btn_timer_start);
        final Button timerResetButton = (Button) findViewById(R.id.btn_timer_reset);
        timerStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    if(isPaused){
                        timerStartButton.setText("Pause");
                        mTimerView.resume();
                    }
                    else {
                        timerStartButton.setText("Resume");
                        mTimerView.pause();
                    }
                    isPaused = !isPaused;
                }
                else{
                    mTimerView.start(TIMER_LENGTH);
                    isRunning = true;
                    timerStartButton.setText("Resume");
                }
            }


        });
        timerResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
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

    @Override
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
    protected void onPause() {
        mTimerView.stop();
        super.onPause();
    }
}