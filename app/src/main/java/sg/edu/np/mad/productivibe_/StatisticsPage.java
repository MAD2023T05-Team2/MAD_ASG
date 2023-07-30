package sg.edu.np.mad.productivibe_;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatisticsPage extends AppCompatActivity {
    final String TITLE = "Statistics Page";

    private LineChart moodChart;
    ImageView imageView;
    ImageView frogWaveImageView;
    TextView totalFocusTimeTextView;

    private DatabaseReference taskDBR;
    private FirebaseDatabase fdb;
    private ArrayList<Task> doneTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_page);
        Log.v(TITLE, "Navigation Buttons");

        // Bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_statistics);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_statistics) {
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(StatisticsPage.this, HomePage.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(StatisticsPage.this, TaskTimerPage.class));
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(StatisticsPage.this, TaskActivity.class));
                return true;
            } else if (itemId == R.id.bottom_destress) {
                startActivity(new Intent(StatisticsPage.this, DestressPage.class));
                return true;
            }
            return false;
        });

        // For the Timer statistics
        imageView = findViewById(R.id.imageview);
        totalFocusTimeTextView = findViewById(R.id.totalFocusTimeTextView);
        frogWaveImageView = findViewById(R.id.frogWaveImageView);

        // Adding the gif here using Glide library
        Glide.with(this).load(R.drawable.confetti).into(imageView);
        Glide.with(this).load(R.drawable.kermit_wave).into(frogWaveImageView);

        // For the mood statistics
        moodChart = findViewById(R.id.moodChart);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call the method to calculate and update the total focus time
        calculateTotalFocusTime();

        // drawing the Line Chart
        List<String> dates = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();

        //for comparing
        // Calculate the date one month ago from the current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        long lastMonthTimeStamp = calendar.getTime().getTime(); // take all posts after this timing
        // Start up Firebase reference
        // Retrieve mood data from the database
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //String userName = sharedPreferences.getString("Username", null);
        String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference moodDBR = fdb.getReference("moods/" + userName);
        moodDBR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Create lists to store the x-axis values (dates) and y-axis values (moods)
                entries.clear();
                List<String> moodsList = new ArrayList<>();

                // Format the timestamp from Firebase to a string
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                // retrieval
                for (DataSnapshot sn: snapshot.getChildren()){
                    if (Long.parseLong(sn.getKey()) > lastMonthTimeStamp){
                        String mV = sn.getValue(String.class);
                        // save as date format NOT long
                        Date ts = new Date(Long.parseLong(sn.getKey()));
                        String TimeStamp = dateFormat.format(ts);
                        dates.add(formatDate(TimeStamp));
                        moodsList.add(mV);
                    }
                }
                Log.d("Firebase MOOD", String.valueOf(moodsList.size()));
                Log.d("Firebase dates", String.valueOf(dates.size()));

                // sort the dates in ascending order in dates
                Collections.sort(dates, (date1, date2) -> {
                    SimpleDateFormat format = new SimpleDateFormat("dd MMM", Locale.getDefault());
                    try {
                        Date d1 = format.parse(date1);
                        Date d2 = format.parse(date2);
                        return d1.compareTo(d2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                });

                // Create entries for the line chart
                for (int i = 0; i < moodsList.size(); i++) {
                    String mood = moodsList.get(i);
                    float moodValue = convertMoodToValue(mood);
                    entries.add(new Entry(i, moodValue));
                }
                Log.d("Firebase entries", String.valueOf(entries.size()));

                LineDataSet dataSet = new LineDataSet(entries, "Mood");
                LineData lineData = new LineData(dataSet);

                drawLineChart(dataSet,dates);

                moodChart.notifyDataSetChanged();
                moodChart.setData(lineData);
                // Refresh the chart
                moodChart.invalidate();
                Log.i(TITLE,"Refresh!");
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if cannot connect or firebase returns an error
                Toast.makeText(getApplicationContext(), "You're offline :( Cannot reach the database", Toast.LENGTH_SHORT).show();
                Log.d(TITLE, error.getMessage());
            }
        });

    }

    // Helper method to convert mood string to a numerical value
    private float convertMoodToValue(String mood) {
        switch (mood) {
            case "party":
                return 4f;
            case "happy":
                return 3f;
            case "neutral":
                return 2f;
            case "sad":
                return 1f;
            default:
                return 0f;
        }
    }

    // Helper method to format the date
    private String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

        try {
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    // Method to draw and customize the line chart
    private void drawLineChart(LineDataSet dataSet, List<String> dates){

        // Create a LineDataSet from the entries
        dataSet.setDrawValues(false); // Disable displaying values on data points
        dataSet.setColor(Color.BLACK); // Set line color
        dataSet.setLineWidth(3f); // Set line width
        dataSet.setCircleRadius(3f); // Set circle radius
        dataSet.setCircleColor(Color.BLACK); // Set circle color
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Set line mode to curved
        Log.i(TITLE,"Create a LineDataSet from the entries");


        // Customize the appearance of the line chart
        LineData lineData = new LineData(dataSet);
        moodChart.setData(lineData);
        moodChart.getDescription().setEnabled(false); // Disable chart description
        moodChart.getLegend().setEnabled(false); // Disable legend
        moodChart.setTouchEnabled(false); // Disable touch interactions
        moodChart.setPinchZoom(false); // Disable pinch zoom
        moodChart.setScaleEnabled(false); // Disable scaling
        moodChart.notifyDataSetChanged();
        Log.i(TITLE,"Customize the appearance of the line chart");


        // Customize the X-axis
        XAxis xAxis = moodChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set X-axis position
        xAxis.setGranularity(1f); // Set X-axis granularity
        xAxis.setTextSize(12f); // Set X-axis label text size
        xAxis.setTextColor(Color.BLACK); // Set X-axis label text color
        xAxis.setDrawAxisLine(false); // Hide X-axis line
        xAxis.setDrawGridLines(false); // Hide X-axis grid lines
        xAxis.setYOffset(-2f); // Move X-axis labels higher
        moodChart.setExtraRightOffset(25f); // Shift the last label to the left
        Log.i(TITLE,"Customize the X-axis");

        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Check if there are 5 or fewer dates
                if (dates.size() <= 5) {
                    int index = Math.round(value);
                    if (index >= 0 && index < dates.size()) {
                        return dates.get(index);
                    }
                } else {
                    // Display only 5 evenly spaced dates
                    int index = Math.round(value);
                    if (index >= 0 && index < dates.size()) {
                        int maxInterval = Math.max((dates.size() - 1) / 4, 1);
                        int interval = Math.min((dates.size() - 1) / 4, maxInterval);
                        if (index % interval == 0) {
                            return dates.get(index);
                        } else {
                            return "";
                        }
                    }
                }
                return "";
            }
        });


        // Customize the Y-axis
        String[] moodLabels = {"\uD83D\uDE21", "\uD83D\uDE41", "\uD83D\uDE10","\uD83D\uDE42", "\uD83E\uDD73"}; // Adjusted order
        YAxis yAxis = moodChart.getAxisLeft(); // Get reference to the left axis
        yAxis.setGranularity(1f); // Set Y-axis granularity
        yAxis.setLabelCount(moodLabels.length); // Set the number of Y-axis labels to the number of moods
        yAxis.setTextSize(22f); // Set Y-axis label text size
        yAxis.setDrawAxisLine(false); // Show Y-axis line

        // Provide the Y-axis labels directly
        yAxis.setValueFormatter(new IndexAxisValueFormatter(moodLabels));


        // Set the minimum and maximum values for the Y-axis
        yAxis.setAxisMaximum(moodLabels.length - 0.4f);

        // Enable grid lines for all rows on the Y-axis
        moodChart.getAxisLeft().setDrawGridLines(true);
        moodChart.getAxisRight().setDrawGridLines(true);
        Log.i(TITLE,"Customize the Y-axis");

        // Set grid line properties
        moodChart.getAxisLeft().setGridLineWidth(1f);
        moodChart.getAxisRight().setGridLineWidth(1f);
        moodChart.getAxisLeft().setGridColor(Color.parseColor("#66999999"));
        moodChart.getAxisRight().setGridColor(Color.parseColor("#66999999"));
        moodChart.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);
        moodChart.getAxisRight().enableGridDashedLine(10f, 10f, 0f);

        Log.i(TITLE,"Set grid line properties");

        // Remove numerical values on the secondary Y-axis
        moodChart.getAxisRight().setEnabled(false); // Disable right axis

        // Refresh the chart
        moodChart.invalidate();


    }

    // Method to calculate total focus time from tasks with 'done' status (in minutes)
    private void calculateTotalFocusTime() {
        // Initialize the database;
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //String userName = sharedPreferences.getString("Username", null);
        String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fdb = FirebaseDatabase.getInstance();
        taskDBR = fdb.getReference("tasks/" + userName);
        doneTasks = new ArrayList<>();
        taskDBR.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> taskNames = new ArrayList<>();
                // Calculation of total focus time should be done here
                int totalDurationInMinutes = 0;

                for (DataSnapshot sn : snapshot.getChildren()) {
                    Task t = sn.getValue(Task.class);
                    // Create a list of task names to display in the dialog
                    if (t.getStatus().equalsIgnoreCase("done")) {
                        taskNames.add(t.getTaskName());
                        doneTasks.add(t);
                        // Collecting total duration
                        totalDurationInMinutes += t.getTaskDuration();
                    }
                }

                // Convert the total duration to hours and minutes
                int hours = totalDurationInMinutes / 60;
                int minutes = totalDurationInMinutes % 60;

                // Set the default text if no "done" tasks
                String defaultText = "You are live laugh loving right now!\n" +
                        "Go on and make some tasks before coming back!";

                // Update the TextView with the calculated total focus time or the default text
                if (doneTasks.isEmpty()) {
                    totalFocusTimeTextView.setText(defaultText);
                } else {
                    totalFocusTimeTextView.setText("Total Focus Time\n" + hours + " hours and " + minutes + " minutes\n" + "You have done something, yay!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TITLE,error.getMessage());

            }
        });
    }

}