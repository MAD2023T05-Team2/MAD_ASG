package sg.edu.np.mad.productivibe_;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

//import sg.edu.np.mad.productivibe_.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

// The calendar page allows users to view the different tasks due by dates in a more organised manner
public class CalendarPage extends AppCompatActivity {
    String TITLE = "Calendar Page";
    private List<Task> filteredTaskList = new ArrayList<>();
    private TaskAdapter adapter;
    private DatabaseReference taskDBR;
    private FirebaseDatabase fdb;
    private RecyclerView filteredRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Log.v(TITLE,"Created the page !");

        // Setting the navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_tasks);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(CalendarPage.this, TaskTimerPage.class));
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(CalendarPage.this, TaskActivity.class));
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(CalendarPage.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_destress) {
                startActivity(new Intent(CalendarPage.this, DestressPage.class));
                return true;
            } else if (itemId == R.id.bottom_statistics) {
                startActivity(new Intent(CalendarPage.this, StatisticsPage.class));
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
        filteredRecyclerView.setItemAnimator(null);

        // Initialize the database
        //SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //String userName = sharedPreferences.getString("Username", null);
        fdb = FirebaseDatabase.getInstance();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        taskDBR = fdb.getReference("tasks/"+ userName);
        // Load tasks from the database
        loadTasks(new GetTaskData() {
            @Override
            public void onDataLoaded(List<Task> taskList) {
                //get current date
                Date currentDate = new Date();
                SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
                String crrDate = format.format(currentDate);
                // filter the task list
                // since the calendar would auto select that day
                filteredTaskList = filterListDate(filteredTaskList,crrDate);
                adapter = new TaskAdapter(filteredTaskList);
                filteredRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMsg) {
                Log.d(TITLE,errorMsg);

            }
        });
        // recyclerview
        // watch out for user selecting other dates
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                //formatting
                //String month_name = DateFormatSymbols.getInstance().getMonths()[month].substring(0,3);
                String strDate = String.format("%1$s/%2$s/%3$s", day, (month + 1),year);
                // filter out recycler view
                Log.i(TITLE, strDate);
                // retrieving the filtered values
                loadTasks(new GetTaskData() {
                    @Override
                    public void onDataLoaded(List<Task> taskList) {

                        taskList = filterListDate(taskList,strDate);
                        adapter.notifyItemRangeInserted(0,taskList.size());
                        if (taskList.isEmpty()){
                            //filteredTasksList.clear();
                            adapter.clearList();
                            //adapter.notifyDataSetChanged();
                            Log.d("FILTERED","List is empty");
                        }
                        else {
                            //adapter.clearList();
                            adapter.updateList(taskList);
                            adapter.notifyItemRangeChanged(0,taskList.size());
                            Log.d("FILTERED", "List should not be empty");
                        }
                        Log.v("AFTER FILTERING",taskList.toString());
                    }

                    @Override
                    public void onError(String errorMsg) {
                        Log.d(TITLE,errorMsg);
                    }
                });

                //filteredTaskList = filterListDate(taskDatabase.getAllTasksFromUser(userId),strDate);
                //List<Task> filteredTasksList = taskDatabase.getFilteredTasks("due_date","date",strDate);

            }
        });

        // Switching back from calendar to all tasks view
        FloatingActionButton toAllTasks = findViewById(R.id.toAllTasks);
        toAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarPage.this, TaskActivity.class));
            }
        });
    }

    private void loadTasks(GetTaskData getTaskData){
        filteredTaskList.clear();
        taskDBR.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sn: snapshot.getChildren()){
                    Task t = sn.getValue(Task.class);
                    filteredTaskList.add(t);
                }
                Log.d("FIREBASE",String.valueOf(filteredTaskList.size()));
                getTaskData.onDataLoaded(filteredTaskList);
                //adapter.notifyItemRangeInserted(0,filteredTaskList.size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if cannot connect or firebase returns an error
                Toast.makeText(getApplicationContext(), "You're offline :( Cannot reach the database", Toast.LENGTH_SHORT).show();
                Log.d(TITLE, error.getMessage());
            }
        });
    }

    public List<Task> filterListDate(List<Task> filteredTaskList, String strDate){
        // filter out tasks based on due data
        List<Task> temp = new ArrayList<>();
        SimpleDateFormat comparedformat = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        SimpleDateFormat firebase = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        // usable for both current Date and future dates
        for (Task t : filteredTaskList){
            // check if it contains the date
            Date comparedDate = null;
            try {
                comparedDate = firebase.parse(t.getTaskDueDateTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String compareDate = comparedformat.format(comparedDate);
            if (compareDate.equals(strDate)){
                temp.add(t);
            }
        }
        return temp;
    }
}
