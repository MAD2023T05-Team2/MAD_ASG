package sg.edu.np.mad.productivibe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Drawable;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.model.GradientColor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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

        // Sort the dates in ascending order
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

        // Adjust the top margin to shift the LineChart up
        LineChart lineChart = findViewById(R.id.moodChart);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) lineChart.getLayoutParams();
        int marginTop = -50; // Set the desired margin value for the top
        layoutParams.setMargins(layoutParams.leftMargin, marginTop, layoutParams.rightMargin, layoutParams.bottomMargin);
        lineChart.setLayoutParams(layoutParams);


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
        yAxis.setAxisMinimum(-0.5f);
        yAxis.setAxisMaximum(moodLabels.length - 0.5f);

        // Enable grid lines for all rows on the Y-axis
        moodChart.getAxisLeft().setDrawGridLines(true);
        moodChart.getAxisRight().setDrawGridLines(true);

        // Set grid line properties
        moodChart.getAxisLeft().setGridLineWidth(1f);
        moodChart.getAxisRight().setGridLineWidth(1f);
        moodChart.getAxisLeft().setGridColor(Color.parseColor("#66999999"));
        moodChart.getAxisRight().setGridColor(Color.parseColor("#66999999"));
        moodChart.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);
        moodChart.getAxisRight().enableGridDashedLine(10f, 10f, 0f);

        // Remove numerical values on the secondary Y-axis
        moodChart.getAxisRight().setEnabled(false); // Disable right axis

        // Refresh the chart
        moodChart.invalidate();
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
            case "angry":
                return 0f;
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