package sg.edu.np.mad.productivibe_;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

/*
This activity displays the splash screen that is shown whenever the user opens the application
 */
public class SplashScreen extends AppCompatActivity {
    ProgressBar progBar;
    int counter = 0;
    private static final int SPLASH_DURATION = 2500;
    String TITLE = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBarAnimate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, LoginPage.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DURATION);
    }

    // This animates the progress bar, showing users the loading progress, enhancing user experience
    public void progressBarAnimate() {
        progBar = findViewById(R.id.progressBar);
        final Timer progTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                progBar.setVisibility(View.VISIBLE);
                progBar.setProgress(counter);
                if (counter == 100) {
                    progTimer.cancel();
                }
            }
        };
        progTimer.schedule(timerTask, 0, 30);
        Log.v(TITLE, "Splash done");
    }
}
