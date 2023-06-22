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

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements TaskAdapter.OnItemClickListener, TaskAdapter.OnEditClickListener {

    private RecyclerView homeTaskRecyclerView;
    private TaskDatabase taskDatabase;
    private TaskAdapter homeTaskadapter;
    final String TITLE = "Home Page";
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

        TextView myMessage = findViewById(R.id.textView);
        myMessage.setText("Hello, " + recvName);

        // Initialize the database
        taskDatabase = TaskDatabase.getInstance(this);
        // Load tasks from the database
        List<Task> homeTaskList = taskDatabase.getAllTasks();
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


}