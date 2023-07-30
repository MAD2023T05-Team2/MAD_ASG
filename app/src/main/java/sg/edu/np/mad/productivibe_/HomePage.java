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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private TaskAdapter homeTaskadapter;
    private boolean isMuted;
    private ValueEventListener retrieveData;

    private DatabaseReference taskDBR;
    private FirebaseDatabase fdb;
    private FirebaseAuth uAuth;
    private FirebaseUser currentUser;
    final String TITLE = "HomePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Log.v(TITLE, "Navigation Buttons");

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

        // Using Firebase Auth
        fdb = FirebaseDatabase.getInstance(); // for realtime storage
        uAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = uAuth.getCurrentUser();

        // Showing of "Hello, [name] on homepage

        // Display "Hello, [name]" after being authenticated
        String recvName = currentUser.getDisplayName();
        TextView myMessage = findViewById(R.id.textView);
        myMessage.setText("Hello " + recvName); // Set the text of the TextView to "Hello, " concatenated with the received name


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
        //sharedPreferences = this.getSharedPreferences("MyPrefs", 0);
        //String userName = sharedPreferences.getString("Username", null);
        String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // create list of today task based on the user
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
//        // Create a reference to the "moods" node for the current user
//        DatabaseReference moodDBR = fdb.getReference("moods/" + userName);
//
//        // Delete all moods for the current user
//        moodDBR.removeValue()
//                .addOnSuccessListener(aVoid -> {
//                    // Success callback
//                    Log.d(TITLE, "All moods deleted successfully");
//                    // Add your further actions or message here, if needed.
//                })
//                .addOnFailureListener(e -> {
//                    // Failure callback
//                    Log.e(TITLE, "Failed to delete moods: " + e.getMessage());
//                    // Handle the failure or display an error message here, if needed.
//                });

//        // Create a reference to the "moods" node for the current user
//        DatabaseReference moodDBR = fdb.getReference("moods/" + userName);
//
//        // Manually add mood objects and save them to the database
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//
//        try {
//            Mood mood1 = new Mood(userName, "happy", dateFormat.parse("2023-07-15 10:30:00").getTime());
//            Mood mood2 = new Mood(userName, "neutral", dateFormat.parse("2023-07-18 15:45:00").getTime());
//            Mood mood3 = new Mood(userName, "sad", dateFormat.parse("2023-07-19 09:00:00").getTime());
//            Mood mood4 = new Mood(userName, "party", dateFormat.parse("2023-07-20 19:20:00").getTime());
//            Mood mood5 = new Mood(userName, "angry", dateFormat.parse("2023-07-20 12:10:00").getTime());
//            Mood mood6 = new Mood(userName, "party", dateFormat.parse("2023-07-21 12:10:00").getTime());
//            Mood mood7 = new Mood(userName, "angry", dateFormat.parse("2023-07-26 12:10:00").getTime());
//            Mood mood8 = new Mood(userName, "neutral", dateFormat.parse("2023-07-28 12:10:00").getTime());
//            Mood mood9 = new Mood(userName, "happy", dateFormat.parse("2023-07-08 12:10:00").getTime());
//
//            // Save the mood objects to the database
//            moodDBR.child(String.valueOf(mood1.getTimestamp())).setValue(mood1.getMood());
//            moodDBR.child(String.valueOf(mood2.getTimestamp())).setValue(mood2.getMood());
//            moodDBR.child(String.valueOf(mood3.getTimestamp())).setValue(mood3.getMood());
//            moodDBR.child(String.valueOf(mood4.getTimestamp())).setValue(mood4.getMood());
//            moodDBR.child(String.valueOf(mood5.getTimestamp())).setValue(mood5.getMood());
//            moodDBR.child(String.valueOf(mood6.getTimestamp())).setValue(mood6.getMood());
//            moodDBR.child(String.valueOf(mood7.getTimestamp())).setValue(mood7.getMood());
//            moodDBR.child(String.valueOf(mood8.getTimestamp())).setValue(mood8.getMood());
//            moodDBR.child(String.valueOf(mood9.getTimestamp())).setValue(mood9.getMood());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }




    }

    @Override
    protected void onResume(){
        super.onResume();

        String recvName = currentUser.getDisplayName();
        TextView myMessage = findViewById(R.id.textView);
        myMessage.setText("Hello, " + recvName); // Set the text of the TextView to "Hello, " concatenated with the received name
    }

    private void saveMood(String moodValue) {
        // Initialize the database;
        //SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //String userName = sharedPreferences.getString("Username", null);
        String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
            if (currentUser.getDisplayName() == "Guest"){
                // guest account delete from firebase auth
                // save the uId for deletion in realtime storage
                String uId = currentUser.getUid();
                FirebaseDatabase fdb = FirebaseDatabase.getInstance();
                DatabaseReference forTasks = fdb.getReference("tasks/" + uId );
                forTasks.removeValue();
                DatabaseReference forMoods = fdb.getReference("moods/" + uId);
                forMoods.removeValue();
                currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        // delete from firebase
                        if (task.isSuccessful()){ // ensures Firebase Auth is dealt with
                            uAuth.signOut();
                        }
                        else{
                            Log.w(TITLE,"delete failed", task.getException());
                        }

                    }
                });
            }
            uAuth.signOut(); // Log out FirebaseAuth

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
    protected  void onStop(){
        super.onStop();
        // Release mediaPlayer when the app is shutdown
        // Mark that the app back to launching for the first time
        SharedPreferences sharedPreferences2 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putBoolean("IsFirstLaunch", true);
        editor.apply();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // for the firebase listener
        if (taskDBR != null && retrieveData != null) {
            taskDBR.removeEventListener(retrieveData);
        }
        onDestroy();
    }
}
