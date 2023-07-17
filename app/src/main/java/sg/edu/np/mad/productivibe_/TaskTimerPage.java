package sg.edu.np.mad.productivibe_;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TaskTimerPage extends AppCompatActivity {
    private static final int TIMER_LENGTH = 30;

    private TaskTimerView mTimerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_timer_page);
        mTimerView = (TaskTimerView) findViewById(R.id.timer);

        final Button timerStartButton = (Button) findViewById(R.id.btn_timer_start);
        timerStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerView.start(TIMER_LENGTH);
            }
        });
    }

    @Override
    protected void onPause() {
        mTimerView.stop();
        super.onPause();
    }
}