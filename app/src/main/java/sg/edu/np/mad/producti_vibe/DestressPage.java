package sg.edu.np.mad.producti_vibe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DestressPage extends AppCompatActivity {
    private static final String TITLE = "Destress Page";
    private Button picturesButton;
    private Button videosButton;
    private boolean isCountdownRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destress_page);

        picturesButton = findViewById(R.id.pictures);
        videosButton = findViewById(R.id.videos);

        replaceFragment(new DestressMessage());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_timer);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_timer) {
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(DestressPage.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(DestressPage.this, CalendarPage.class));
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(DestressPage.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(DestressPage.this, ProfilePage.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        picturesButton.setOnClickListener(v -> {
            if (!isCountdownRunning) {
                countDownTimer();
            }
            replaceFragment(new PicturesFragment());
        });

        videosButton.setOnClickListener(v -> {
            if (!isCountdownRunning) {
                countDownTimer();
            }
            replaceFragment(new VideosFragment());
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private CountDownTimer myCountdown;

    private void countDownTimer() {
        isCountdownRunning = true;

        myCountdown = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TITLE, "Countdown: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Log.v(TITLE, "Countdown done!");
                lockedOut();
                myCountdown.cancel();
            }
        };

        myCountdown.start();
    }

    private void lockedOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("1 minute is up! Back to being productive!")
                .setCancelable(false)
                .setPositiveButton("Let's go!", (dialog, which) -> {
                    Log.v(TITLE, "User Accepts");
                    Intent intent = new Intent(DestressPage.this, MainActivity.class);
                    startActivity(intent);
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isCountdownRunning) {
            myCountdown.cancel();
        }
    }

    public static class DestressMessage extends Fragment {
        View view;
        ImageView gif;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_destress_message, container, false);
            gif = view.findViewById(R.id.frog_gif);
            // Adding the gif here using Glide library
            Glide.with(this).asGif().load(R.drawable.frog).into(gif);
            return view;
        }
    }
}
