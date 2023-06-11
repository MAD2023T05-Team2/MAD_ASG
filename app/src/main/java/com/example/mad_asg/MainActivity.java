package com.example.mad_asg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button launchTaskButton = findViewById(R.id.launchTaskButton);
        launchTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTaskActivity();
            }
        });
    }
    private void launchTaskActivity() {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }
    protected  void  onResume(){

        super.onResume();
    }
}