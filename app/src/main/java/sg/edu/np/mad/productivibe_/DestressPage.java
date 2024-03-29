package sg.edu.np.mad.productivibe_;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// The destress page allows users a 5 minute break from their tasks to view entertaining pictures and videos
// and also a punching bag for them to release their stress
public class DestressPage extends AppCompatActivity {
    private static final String TITLE = "Destress Page";
    private Button picturesButton;
    private Button videosButton;
    private Button punchButton;
    private boolean isCountdownRunning = false;
    MediaPlayerManager mediaPlayer = MediaPlayerManager.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destress_page);

        picturesButton = findViewById(R.id.pictures);
        videosButton = findViewById(R.id.videos);
        punchButton = findViewById(R.id.punch);

        // Show fragment with the destress message
        replaceFragment(new DestressMessage());

        // Setting the navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_destress);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_destress) {
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(DestressPage.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(DestressPage.this, TaskTimerPage.class));
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(DestressPage.this, TaskActivity.class));
                return true;
            } else if (itemId == R.id.bottom_statistics) {
                startActivity(new Intent(DestressPage.this, StatisticsPage.class));
                return true;
            }
            return false;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // When picture button is pressed, picture fragment is displayed
        picturesButton.setOnClickListener(v -> {
            if (!isCountdownRunning) { //If countdown timer is not running yet, start countdown timer
                countDownTimer();
            }
            // If pictures fragment is accessed, music continues to play
            replaceFragment(new PicturesFragment());
            Log.v(TITLE, "Picture fragment");
        });

        // When video button is pressed, video fragment is displayed
        videosButton.setOnClickListener(v -> {
            if (!isCountdownRunning) { // If countdown timer is not running yet, start countdown timer
                countDownTimer();
            }
            // Stop the BGM when entering the fragment
            mediaPlayer.pause();
            replaceFragment(new VideosFragment());
            Log.v(TITLE, "Video fragment");
        });

        // When punching bag button is pressed, punch fragment is displayed
        punchButton.setOnClickListener(v -> {
            if (!isCountdownRunning) { // If countdown timer is not running yet, start countdown timer
                countDownTimer();
            }
            replaceFragment(new PunchFragment());
            Log.v(TITLE, "Punching Bag fragment");
        });
    }

    // Function to replace a fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager(); // For managing fragments within the activity
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Start a new fragment transaction
        fragmentTransaction.replace(R.id.frameLayout, fragment); // Replaces current fragment with new fragment
        fragmentTransaction.commit(); // Applying and executing the replacing of fragment
    }

    private CountDownTimer myCountdown;

    private void countDownTimer() {
        isCountdownRunning = true; // Countdown timer is running

        myCountdown = new CountDownTimer(300000, 1000) { // Set countdown timer of 5 minutes
            @Override
            public void onTick(long millisUntilFinished) { // See time left until countdown is finished
                Log.v(TITLE, "Countdown: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() { // When countdown is finished
                Log.v(TITLE, "Countdown done!");
                lockedOut(); // Call alert dialog
                myCountdown.cancel(); // Countdown is cancelled
            }
        };

        myCountdown.start(); // Starts and initiates the countdown process
    }

    private void lockedOut() { // Function to call alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("5 minutes is up! Back to being productive!") // After countdown timer of one minute is finished, this message will pop up
                .setCancelable(false)
                .setPositiveButton("Let's go!", (dialog, which) -> { // User only have one button to accept
                    Log.v(TITLE, "User Accepts");
                    Intent intent = new Intent(DestressPage.this, TaskActivity.class); // Bring user back to the task page after pressing the button
                    startActivity(intent);
                    mediaPlayer.resume();
                });

        AlertDialog alert = builder.create(); // Create alert dialog
        alert.show(); // Show alert dialog
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isCountdownRunning) {
            myCountdown.cancel(); // Cancel the countdown timer if it is running to prevent memory leaks
        }
    }

    // cancel the countdown timer when going into another activity
    @Override
    protected void onStop() {
        super.onStop();
        if (isCountdownRunning && myCountdown != null) {
            myCountdown.cancel();
            isCountdownRunning = false;
        }
    }

    public static class DestressMessage extends Fragment { //Destress message fragment
        View view;
        ImageView gif;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_destress_message, container, false);
            // Find the GIF view in the inflated layout
            gif = view.findViewById(R.id.frog_gif);
            // Adding the gif here using Glide library
            Glide.with(this).asGif().load(R.drawable.frog).into(gif);
            return view;
        }

    }
}

