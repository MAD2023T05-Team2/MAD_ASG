package sg.edu.np.mad.productivibe_;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TaskTimerPage extends AppCompatActivity implements TaskTimerListener {
    private static final int TIMER_LENGTH = 30;
    private boolean isPaused = false;
    private boolean isRunning = false;
    private TaskTimerView mTimerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_timer_page);
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
                    timerStartButton.setText("Pause");

                }
            }


        });
        timerResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerView.stop();
                isRunning = false;
                timerStartButton.setText("Start");
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