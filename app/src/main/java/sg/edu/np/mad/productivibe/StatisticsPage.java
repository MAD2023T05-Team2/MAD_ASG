package sg.edu.np.mad.productivibe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatisticsPage extends AppCompatActivity {
    String TITLE = "Statistics Page";

    private LineChart moodChart;

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
            } else if (itemId == R.id.bottom_calendar) {
                startActivity(new Intent(StatisticsPage.this, CalendarPage.class));
                return true;
            } else if (itemId == R.id.bottom_tasks) {
                startActivity(new Intent(StatisticsPage.this, TaskActivity.class));
                return true;
            } else if (itemId == R.id.bottom_timer) {
                startActivity(new Intent(StatisticsPage.this, DestressPage.class));
                return true;
            }
            return false;
        });

        moodChart = findViewById(R.id.moodChart);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Retrieve mood data from the database
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserId", null);
        List<Mood> moods = Database.getInstance(this).getMoodsForLastMonth(userId);

        // Create lists to store the x-axis values (dates) and y-axis values (moods)
        List<String> dates = new ArrayList<>();
        List<String> moodsList = new ArrayList<>();

        // Prepare the mood data for the chart
        for (Mood mood : moods) {
            dates.add(formatDate(mood.getTimestamp()));
            moodsList.add(mood.getMood());
        }

        // Create entries for the line chart
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < moodsList.size(); i++) {
            String mood = moodsList.get(i);
            float moodValue = convertMoodToValue(mood);
            entries.add(new Entry(i, moodValue));
        }

        // Create a LineDataSet from the entries
        LineDataSet dataSet = new LineDataSet(entries, "Mood");
        dataSet.setDrawValues(false); // Disable displaying values on data points
        dataSet.setColor(Color.BLACK); // Set line color
        dataSet.setLineWidth(3f); // Set line width
        dataSet.setCircleRadius(3f); // Set circle radius
        dataSet.setCircleColor(Color.BLACK); // Set circle color
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Set line mode to curved

        // Customize the appearance of the line chart
        LineData lineData = new LineData(dataSet);
        moodChart.setData(lineData);
        moodChart.getDescription().setEnabled(false); // Disable chart description
        moodChart.getLegend().setEnabled(false); // Disable legend
        moodChart.setTouchEnabled(false); // Disable touch interactions
        moodChart.setPinchZoom(false); // Disable pinch zoom
        moodChart.setScaleEnabled(false); // Disable scaling

        // Customize the X-axis
        XAxis xAxis = moodChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set X-axis position
        xAxis.setGranularity(1f); // Set X-axis granularity
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Display only 4 evenly spaced dates
                int index = Math.round(value);
                if (index >= 0 && index < dates.size()) {
                    int interval = (dates.size() - 1) / 3;
                    if (index % interval == 0) {
                        return dates.get(index);
                    } else {
                        return "";
                    }
                }
                return "";
            }
        });

        // Customize the Y-axis
        YAxis yAxis = moodChart.getAxisLeft(); // Get reference to the left axis
        yAxis.setGranularity(1f); // Set Y-axis granularity
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Customize the Y-axis labels based on the mood values
                String[] moodLabels = {"Angry", "Neutral", "Sad", "Happy", "Party"}; // Adjusted order
                int index = Math.round(value);
                if (index >= 0 && index < moodLabels.length) {
                    return moodLabels[index];
                }
                return "";
            }
        });

        // Remove numerical values on the secondary Y-axis
        moodChart.getAxisRight().setEnabled(false); // Disable right axis

        // Customize the grid lines
        moodChart.getAxisLeft().setDrawGridLines(true); // Enable Y-axis grid lines
        moodChart.getXAxis().setDrawGridLines(false); // Disable X-axis grid lines


        // Set grid line color and style
        yAxis.setGridColor(Color.parseColor("#80CCCCCC")); // Set grid line color
        yAxis.enableGridDashedLine(10f, 10f, 0f); // Enable dashed grid lines

        // Refresh the chart
        moodChart.invalidate();
    }

    // Helper method to convert mood string to a numerical value
    private float convertMoodToValue(String mood) {
        switch (mood) {
            case "Party":
                return 0f;
            case "Happy":
                return 1f;
            case "Neutral":
                return 2f;
            case "Sad":
                return 3f;
            case "Angry":
                return 4f;
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
}
