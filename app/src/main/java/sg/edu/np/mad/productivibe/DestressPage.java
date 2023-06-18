package sg.edu.np.mad.productivibe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DestressPage extends AppCompatActivity {
    final String TITLE = "Destress Page";
    Button vidFragButton, picFragButton;
    boolean isCountdownRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destress_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_timer);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_timer) {
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(DestressPage.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(DestressPage.this, CalendarPage.class));
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(DestressPage.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(DestressPage.this, MainActivity.class));
                return true;
            }
            return false;
        });

        picFragButton = findViewById(R.id.pictures);
        vidFragButton = findViewById(R.id.videos);

        replaceFragment(new DestressMessage());

        picFragButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCountdownRunning) {
                    countDownTimer();
                }
                replaceFragment(new Pictures());
            }
        });

        vidFragButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCountdownRunning) {
                    countDownTimer();
                }
                replaceFragment(new Videos());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    CountDownTimer myCountdown;
    private void countDownTimer(){

        isCountdownRunning = true;

        myCountdown = new CountDownTimer(60000,1000){
            @Override
            public void onTick(long l){
                Log.v(TITLE, "Countdown" + l/1000);
            }
            @Override
            public void onFinish(){
                Log.v(TITLE, "countdown done!");
                lockedOut();
                myCountdown.cancel();
            }
        };
        myCountdown.start();
    }

    private void lockedOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("1 minute is up! Back to being productive!").setCancelable(false);
        builder.setPositiveButton("Let's go!", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                Log.v(TITLE, "User Accepts");
                Intent intent = new Intent(DestressPage.this, HomePage.class);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

//    RecyclerView dpRecyclerView = findViewById(R.id.pictureRecyclerView);
//    DestressAdapter destressAdapter = new DestressAdapter(imageList);
//    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//    dpRecyclerView.setLayoutManager(layoutManager);
//    dpRecyclerView.setItemAnimator(new DefaultItemAnimator());
//    dpRecyclerView.setAdapter(destressAdapter);
}