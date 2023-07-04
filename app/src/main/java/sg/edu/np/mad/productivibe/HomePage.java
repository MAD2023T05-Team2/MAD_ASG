package sg.edu.np.mad.productivibe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

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
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(HomePage.this, CalendarPage.class));
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
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_edit_profile) {
            // To edit user's profile details
            startActivity(new Intent(HomePage.this, EditProfilePage.class));
            return true;
        }
        else if (itemId == R.id.menu_music_settings) {
            // To music settings
            startActivity(new Intent(HomePage.this, MusicSettingsPage.class));
            return true;
        }
        else if (itemId == R.id.menu_logout) {
            // To log out
            SharedPreferences rememberUserData = getSharedPreferences("MyPrefs", MODE_PRIVATE); // Updates remember option to "False"
            SharedPreferences.Editor RUDeditor = rememberUserData.edit();
            RUDeditor.putString("Remember", "False");
            RUDeditor.apply();
            // Back to login page
            startActivity(new Intent(HomePage.this, LoginPage.class));
            Log.v(TITLE, "Logging out");
            return true;
        }
        return false;
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