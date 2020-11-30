package com.srikant.taskit.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.srikant.taskit.R;

public class TaskView extends AppCompatActivity {
    int day = 0;
    int month = 0;
    int year = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_display);
        Bundle extras = getIntent().getExtras();
        day = extras.getInt("day");
        month = extras.getInt("month");
        year = extras.getInt("year");
        Log.d("DAYMOYR", "" + day + month + year);

    }
}
