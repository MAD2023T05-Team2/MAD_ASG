package com.example.mad_asg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView and its adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(adapter);

        // Example task data
        Task task1 = new Task(1, "Pending", "Task 1", "Task 1 Description",
                new Date(), "10:00 AM", "11:00 AM", 60,
                "Type 1", "Repeat 1", 0, "", 1);
        Task task2 = new Task(2, "Completed", "Task 2", "Task 2 Description",
                new Date(), "12:00 PM", "1:00 PM", 60,
                "Type 2", "Repeat 2", 0, "", 1);

        // Add tasks to the list
        taskList.add(task1);
        taskList.add(task2);
        adapter.notifyDataSetChanged();

        // Example button actions
        Button createButton = findViewById(R.id.createButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code for creating a new task
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = adapter.getSelectedPosition();
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    showDeleteConfirmationDialog(selectedPosition);
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        adapter.setSelectedPosition(position);
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask(position);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void deleteTask(int position) {
        taskList.remove(position);
        adapter.setSelectedPosition(RecyclerView.NO_POSITION);
        adapter.notifyDataSetChanged();
    }
}

