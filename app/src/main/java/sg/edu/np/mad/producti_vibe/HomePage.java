package sg.edu.np.mad.producti_vibe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomePage extends AppCompatActivity implements TaskAdapter.OnItemClickListener, TaskAdapter.OnEditClickListener {

    private RecyclerView homeTaskRecyclerView;
    private TaskDatabase taskDatabase;
    private TaskAdapter homeTaskadapter;
    final String TITLE = "HomePage";
    String recvName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Log.v(TITLE, "Navigation Buttons");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(HomePage.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(HomePage.this, CalendarPage.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(HomePage.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(HomePage.this, ProfilePage.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String recvName = sharedPreferences.getString("Name", null);
        Log.d(TITLE, "Received name from SharedPreferences: " + recvName);

        TextView myMessage = findViewById(R.id.textView);
        myMessage.setText("Hello, " + recvName);

        // Initialize the database
        taskDatabase = TaskDatabase.getInstance(this);
        // Load tasks from the database
        //homeTaskList = taskDatabase.getAllTasks();
        List<Task> homeTaskList = filterCurrentDate(taskDatabase.getAllTasks());
        // recyclerview
        homeTaskRecyclerView = findViewById(R.id.homeTaskRecyclerView);
        homeTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeTaskadapter = new TaskAdapter(homeTaskList, this::onItemClick, this::onEditClick);
        homeTaskRecyclerView.setAdapter(homeTaskadapter);
        homeTaskadapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        if (homeTaskadapter.getSelectedPosition() == position){
            position = homeTaskRecyclerView.NO_POSITION;
        }
            homeTaskadapter.setSelectedPosition(position);

    }

    @Override
    public void onEditClick(int position) {
        Log.i(TITLE,"Trying to edit click?????");
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