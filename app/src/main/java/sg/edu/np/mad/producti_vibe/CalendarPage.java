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
    private List<Task> filteredTaskList;
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
        filteredTaskList = taskDatabase.getAllTasks();
        for (Task t: filteredTaskList){
            Log.d("LIST",t.getTaskDueDateTime().toString());
        }
        filteredTaskList.clear();
        adapter = new TaskAdapter(filteredTaskList, this::onItemClick, this::onEditClick);
        filteredRecyclerView.setAdapter(adapter);

        // recyclerview
        // watch out for user selecting other dates
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                //formatting
                //String month_name = DateFormatSymbols.getInstance().getMonths()[month].substring(0,3);
                String strDate = String.format("%1$s-%2$s-%3$s", day, (month + 1),year);
                // filter out recycler view
                Log.i(TITLE, strDate);
                // retrieving the filtered values
                filteredTaskList = filterListDate(taskDatabase.getAllTasks(),strDate);
                //List<Task> filteredTasksList = taskDatabase.getFilteredTasks("due_date","date",strDate);
                if (filteredTaskList.isEmpty()){
                    //filteredTasksList.clear();
                    adapter.clearList();
                    //adapter.notifyDataSetChanged();
                    Log.d("FILTERED","List is empty");
                }
                else{
                    //adapter.clearList();
                    adapter.notifyDataSetChanged();
                    Log.d("FILTERED", "List should not be empty");
                }
                Log.v("AFTER FILTERING",filteredTaskList.toString());
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

    public List<Task> filterListDate(List<Task> filteredTaskList, String strDate){
        // filter out tasks based on due data
        List<Task> temp = new ArrayList<>();
        Log.d("TRYING TO DEBUG", String.valueOf(filteredTaskList.size()));
        // convert date object to a string with a nicer format
        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy");
        for (Task t : filteredTaskList){
            // check if it contains the date
            String comparedDate = format.format(t.getTaskDueDateTime());
            Log.d("FILTERING",comparedDate);
            Log.d("FILTERING",strDate);
            if (comparedDate.equals(strDate)){
                temp.add(t);
            }
        }
        return temp;
    }
}
