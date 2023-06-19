package sg.edu.np.mad.producti_vibe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import sg.edu.np.mad.producti_vibe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormatSymbols;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

import androidx.recyclerview.widget.RecyclerView;


public class CalendarPage extends AppCompatActivity implements TaskAdapter.OnItemClickListener, TaskAdapter.OnEditClickListener {

    String TITLE = "Calendar Page";

    private RecyclerView filteredRecyclerView;
    private ArrayList<Task> filteredTaskList;
    private TaskAdapter adapter;
    private TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Log.v(TITLE,"Created the page !");


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

        filteredRecyclerView = findViewById(R.id.calendarTaskView);
        filteredRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the database
        taskDatabase = TaskDatabase.getInstance(this);
        // Load tasks from the database
        List<Task> filteredTaskList = taskDatabase.getAllTasks();
        adapter = new TaskAdapter(filteredTaskList, this::onItemClick, this::onEditClick);
        filteredRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        // recyclerview
        // watch out for user selecting other dates
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                //formatting
                String month_name = DateFormatSymbols.getInstance().getMonths()[month].substring(0,3);
                String strDate = String.format("%1$s-%2$s-%3$s", day, month_name,year);
                // filter out recycler view
                Log.i(TITLE, strDate);
                // retrieving the filtered values
                List<Task> filteredTasksList = taskDatabase.getFilteredTasks("due_date","date",strDate);
                adapter.setFilter(filteredTasksList);
                adapter.notifyDataSetChanged();
                Log.v(TITLE,filteredTasksList.toString());
            }
        });

    }


    @Override
    public void onItemClick(int position) {
        adapter.setSelectedPosition(position);
    }

    @Override
    public void onEditClick(int position) {
        Log.i(TITLE,"Trying to edit click?????");
    }
}
