package sg.edu.np.mad.productivibe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final VideoView videoView = findViewById(R.id.splashScreen);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splashscreen);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                videoView.start();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                videoView.stopPlayback();
                Intent mainIntent = new Intent(SplashScreen.this, HomePage.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
