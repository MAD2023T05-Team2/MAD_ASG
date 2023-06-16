package com.example.mad_asg;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class CalendarView extends AppCompatActivity {

    String TITLE = "Calendar View";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Log.v(TITLE,"Created the page !");
    }

}
