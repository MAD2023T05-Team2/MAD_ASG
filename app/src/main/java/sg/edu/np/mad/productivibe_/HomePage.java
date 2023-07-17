package sg.edu.np.mad.productivibe_;

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
    final String TITLE = "HomePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Log.v(TITLE, "Navigation Buttons");
        MediaPlayerManager mediaPlayer = MediaPlayerManager.getInstance(this);

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
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(HomePage.this, TaskTimerPage.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(HomePage.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_statistics) {
                startActivity(new Intent(HomePage.this, StatisticsPage.class));
                return true;
            }
            return false;
        });

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

        // Initialize the database
        db = Database.getInstance(this);

        // Load tasks from the database
        String userId = sharedPreferences.getString("UserId", null);
        List<Task> homeTaskList = filterCurrentDate(db.getAllTasksFromUser(userId));

        // Recyclerview to show tasks on homepage
        homeTaskRecyclerView = findViewById(R.id.homeTaskRecyclerView);
        homeTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeTaskadapter = new TaskAdapter(homeTaskList);
        homeTaskRecyclerView.setAdapter(homeTaskadapter);
        homeTaskadapter.notifyDataSetChanged();

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
//        Mood mood1 = new Mood(userId, "happy", "2023-06-28 10:30:00");
//        Mood mood2 = new Mood(userId, "neutral", "2023-06-29 15:45:00");
//        Mood mood3 = new Mood(userId, "sad", "2023-07-01 09:00:00");
//        Mood mood4 = new Mood(userId, "party", "2023-07-03 19:20:00");
//        Mood mood5 = new Mood(userId, "angry", "2023-07-05 12:10:00");
//        Mood mood6 = new Mood(userId, "party", "2023-07-05 12:10:00");
//        Mood mood7 = new Mood(userId, "angry", "2023-07-05 12:10:00");
//        Mood mood8 = new Mood(userId, "neutral", "2023-07-06 12:10:00");
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
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserId", null);
        Date timestamp = new Date();

        // Convert the timestamp to a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestampString = dateFormat.format(timestamp);

        // Create a new Mood object
        Mood mood = new Mood(userId, moodValue, timestampString);

        // Add the mood to the database
        db.addMood(mood);

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
        onDestroy();
        // Mark that the app back to launching for the first time
        SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putBoolean("IsFirstLaunch", true);
        editor.apply();
    }

    public List<Task> filterCurrentDate(List<Task> filteredTaskList){
        // filter out tasks based on due data
        List<Task> temp = new ArrayList<>();
        // convert date object to a string with a nicer format
        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
        //get current date
        Date currentDate = new Date();
        String strDate = format.format(currentDate);
        for (Task t : filteredTaskList){
            // check if it contains the date
            String comparedDate = format.format(t.getTaskDueDateTime());
            if (comparedDate.equals(strDate)){
                temp.add(t);
            }
        }
        return temp;
    }
}
