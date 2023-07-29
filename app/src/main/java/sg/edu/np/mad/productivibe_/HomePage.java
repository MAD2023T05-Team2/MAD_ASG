package sg.edu.np.mad.productivibe_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Homepage showcases the tasks due on that specific day, as well as prompts for user's current mood
public class HomePage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private RecyclerView homeTaskRecyclerView;
    private Database db;
    private TaskAdapter homeTaskadapter;
    private boolean isMuted;
    private ValueEventListener retrieveData;

    private DatabaseReference taskDBR;
    private FirebaseDatabase fdb;
    final String TITLE = "HomePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Log.v(TITLE, "Navigation Buttons");

        // Initialize the database
        db = Database.getInstance(this);

        // Setting the navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(HomePage.this, TaskActivity.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(HomePage.this, TaskTimerPage.class));
                return true;
            } else if (itemId == R.id.bottom_destress) {
                startActivity(new Intent(HomePage.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_statistics) {
                startActivity(new Intent(HomePage.this, StatisticsPage.class));
                return true;
            }
            return false;
        });

        MediaPlayerManager mediaPlayer = MediaPlayerManager.getInstance(this);
        // Check if it's the first launch of the app
        SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFirstLaunch = sharedPreferences2.getBoolean("IsFirstLaunch", true);
        Log.v(TITLE, "IsFirstLaunch value: " + isFirstLaunch);

        if (isFirstLaunch) {
            // Start the BGM MediaPlayer and save the state in SharedPreferences
            mediaPlayer.setMusicSource(this, R.raw.bgm);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            Log.v(TITLE, "Music Started!");

            // Mark that the app has already been launched for the first time
            SharedPreferences.Editor editor = sharedPreferences2.edit();
            editor.putBoolean("IsFirstLaunch", false);
            editor.apply();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Showing of "Hello, [name] on homepage
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE); // Retrieve the SharedPreferences object named "MyPrefs" with private access mode
        String recvName = sharedPreferences.getString("Name", null); // Get the value associated with the key "Name" from the SharedPreferences, defaulting to null if not found
        Log.d(TITLE, "Received name from SharedPreferences: " + recvName);

        TextView myMessage = findViewById(R.id.textView);
        myMessage.setText("Hello, " + recvName); // Set the text of the TextView to "Hello, " concatenated with the received name


        // Recyclerview to show tasks on homepage
        homeTaskRecyclerView = findViewById(R.id.homeTaskRecyclerView);
        homeTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get list of tasks
        ArrayList<Task> homeTaskList = new ArrayList<>();

        homeTaskadapter = new TaskAdapter(homeTaskList);
        homeTaskRecyclerView.setAdapter(homeTaskadapter);
        homeTaskadapter.notifyDataSetChanged();

        // Initialize the database
        // Get UserId from shared preferences and put today's tasks into a list
        sharedPreferences = this.getSharedPreferences("MyPrefs", 0);
        String userName = sharedPreferences.getString("Username", null);
        // create list of today task based on the user
        fdb = FirebaseDatabase.getInstance();
        taskDBR = fdb.getReference("tasks/" + userName);
        retrieveData = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // filter to current date
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat firebase = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
                //get current date
                Date currentDate = new Date();
                String strDate = format.format(currentDate);
                for (DataSnapshot sn: snapshot.getChildren()){
                    Task t = sn.getValue(Task.class);
                    Date comparedDate = null;
                    try {
                        comparedDate = firebase.parse(t.getTaskDueDateTime());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    String compareDate = format.format(comparedDate);
                    if (compareDate.equals(strDate)){
                        homeTaskList.add(t);
                    }
                }
                homeTaskadapter.notifyItemRangeInserted(0,homeTaskList.size());
                Log.d("FIREBASE",String.valueOf(homeTaskList.size()));
                // collects all the tasks saved in the firebase
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if cannot connect or firebase returns an error
                //Toast.makeText(getApplicationContext(), "You're offline :( Cannot reach the database", Toast.LENGTH_SHORT).show();
            }
        };
        taskDBR.orderByKey().addListenerForSingleValueEvent(retrieveData);



        // FAB dropdown list
        FloatingActionButton dropdownList = findViewById(R.id.dropdownList);
        dropdownList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflates the list of options
                PopupMenu popupMenu = new PopupMenu(HomePage.this, v);
                popupMenu.setOnMenuItemClickListener(HomePage.this);
                popupMenu.inflate(R.menu.homepage_dropdown_menu);
                popupMenu.show();
            }
        });

        // Add click listeners to mood icons
        ImageView happyIcon = findViewById(R.id.happyIcon);
        ImageView sadIcon = findViewById(R.id.sadIcon);
        ImageView neutralIcon = findViewById(R.id.neutralIcon);
        ImageView angryIcon = findViewById(R.id.angryIcon);
        ImageView partyIcon = findViewById(R.id.partyIcon);

        happyIcon.setOnClickListener(v -> saveMood("happy"));
        sadIcon.setOnClickListener(v -> saveMood("sad"));
        neutralIcon.setOnClickListener(v -> saveMood("neutral"));
        angryIcon.setOnClickListener(v -> saveMood("angry"));
        partyIcon.setOnClickListener(v -> saveMood("party"));



        //FOR TESTING WILL DELETE AFTER
//        db.deleteAllMoods(userId);
//        Mood mood1 = new Mood(userId, "happy", "2023-07-15 10:30:00");
//        Mood mood2 = new Mood(userId, "neutral", "2023-07-18 15:45:00");
//        Mood mood3 = new Mood(userId, "sad", "2023-07-19 09:00:00");
//        Mood mood4 = new Mood(userId, "party", "2023-07-20 19:20:00");
//        Mood mood5 = new Mood(userId, "angry", "2023-07-20 12:10:00");
//        Mood mood6 = new Mood(userId, "party", "2023-07-21 12:10:00");
//        Mood mood7 = new Mood(userId, "angry", "2023-07-26 12:10:00");
//        Mood mood8 = new Mood(userId, "neutral", "2023-07-28 12:10:00");
//        Mood mood9 = new Mood(userId, "happy", "2023-07-08 12:10:00");
//        db.addMood(mood1);
//        db.addMood(mood2);
//        db.addMood(mood3);
//        db.addMood(mood4);
//        db.addMood(mood5);
//        db.addMood(mood6);
//        db.addMood(mood7);
//        db.addMood(mood8);
//        db.addMood(mood9);


    }

    private void saveMood(String moodValue) {
        // Initialize the database;
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("Username", null);
        // = FirebaseDatabase.getInstance();
        DatabaseReference moodDBR = fdb.getReference("moods/" + userName);


        // Convert the timestamp to a string
        Date timestamp = new Date();
        long currentTime = timestamp.getTime();

        // Create a new Mood object
        //Mood mood = new Mood(userName, moodValue, currentTime);
        // unnecessary at this point

        // Add the mood to the database
        moodDBR.child(String.valueOf(currentTime)).setValue(moodValue);

        Toast.makeText(this, "Mood saved: " + moodValue, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        MediaPlayerManager mediaPlayer = MediaPlayerManager.getInstance(this);
        int itemId = item.getItemId();
        if (itemId == R.id.menu_edit_profile) {
            // To edit user's profile details
            startActivity(new Intent(HomePage.this, EditProfilePage.class));
            return true;
        }
        else if (itemId == R.id.menu_music_settings) {
            // Toggles between play/pause of BGM
            if (isMuted) {
                mediaPlayer.resume();
                isMuted = false;
                Log.v(TITLE, "Music Resumed");
            } else {
                mediaPlayer.pause();
                isMuted = true;
                Log.v(TITLE, "Music Paused");
            }
            return true;
        }
        else if (itemId == R.id.menu_logout) {
            // To log out
            SharedPreferences rememberUserData = getSharedPreferences("MyPrefs", MODE_PRIVATE); // Updates remember option to "False"
            SharedPreferences.Editor RUDeditor = rememberUserData.edit();
            RUDeditor.putString("Remember", "False");
            RUDeditor.apply();

            // Mark that the app back to launching for the first time
            SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences2.edit();
            editor.putBoolean("IsFirstLaunch", true);
            editor.apply();

            // Back to login page
            startActivity(new Intent(HomePage.this, LoginPage.class));
            // Release mediaPlayer when user logs out
            mediaPlayer.onDestroy();
            Log.v(TITLE, "Logging out");
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release mediaPlayer when the app is shutdown
        // Mark that the app back to launching for the first time
        SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putBoolean("IsFirstLaunch", true);
        editor.apply();

        // for the firebase listener
        if (taskDBR != null && retrieveData != null) {
            taskDBR.removeEventListener(retrieveData);
        }

        onDestroy();
    }
}
