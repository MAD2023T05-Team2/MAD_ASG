package sg.edu.np.mad.producti_vibe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import sg.edu.np.mad.producti_vibe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class CalendarPage extends AppCompatActivity {

    String TITLE = "Calendar Page";

    private RecyclerView recyclerView;
    private ArrayList<Task> taskList;
    private TaskAdapter adapter;

    private TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Log.v(TITLE,"Created the page !");

        recyclerView = findViewById(R.id.calendarTaskView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_calendar);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_calendar) {
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(CalendarPage.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(CalendarPage.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(CalendarPage.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(CalendarPage.this, MainActivity.class));
                return true;
            }
            return false;
        });


    }

    @Override
    protected void onStart(){
        super.onStart();

        // watch out for user selecting other dates
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {

                // filter out recycler view
                Log.i(TITLE, " " + String.valueOf(day) + String.valueOf(month) + String.valueOf(year));

            }
        });
    }


}
