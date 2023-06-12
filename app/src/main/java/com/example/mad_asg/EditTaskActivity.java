package com.example.mad_asg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditTaskActivity extends AppCompatActivity {

    private Task task;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText dateEditText;
    private EditText startTimeEditText;
    private EditText endTimeEditText;
    // Add EditText fields for other task properties

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Retrieve the task object passed from the previous activity
        Task task = (Task) getIntent().getSerializableExtra("task");


        // Initialize EditText fields
        titleEditText = findViewById(R.id.edit_title);
        descriptionEditText = findViewById(R.id.edit_description);
        dateEditText = findViewById(R.id.edit_date);
        startTimeEditText = findViewById(R.id.edit_start_time);
        endTimeEditText = findViewById(R.id.edit_end_time);
        // Initialize other EditText fields

        // Set the initial values of the task in the UI
        titleEditText.setText(task.getTaskName());
        descriptionEditText.setText(task.getTaskDesc());
        //dateEditText.setText(task.getTaskDate());
        startTimeEditText.setText(task.getTaskStartTime());
        endTimeEditText.setText(task.getTaskEndTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(task.getTaskDate());
        dateEditText.setText(formattedDate);

        // Set other initial values for the task properties

        // Handle the save button click
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the task with the new values
                String newTitle = titleEditText.getText().toString();
                String newDescription = descriptionEditText.getText().toString();
                //Date newDate = dateEditText.getText().toString();
                String newStartTime = startTimeEditText.getText().toString();
                String newEndTime = endTimeEditText.getText().toString();

                String dateString = dateEditText.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date newDate = null;

                try {
                    newDate = dateFormat.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (newDate != null) {
                    // Date parsing successful, continue with your logic
                } else {
                    // Date parsing failed, handle the error
                }
                // Update other task properties

                task.setTaskName(newTitle);
                task.setTaskDesc(newDescription);
                task.setTaskDate(newDate);
                task.setTaskStartTime(newStartTime);
                task.setTaskEndTime(newEndTime);
                // Update other task properties

                // Pass the updated task back to the previous activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("task", task);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}

