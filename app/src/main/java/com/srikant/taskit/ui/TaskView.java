package com.srikant.taskit.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.srikant.taskit.R;
import com.srikant.taskit.util.SessionData;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class TaskView extends AppCompatActivity {
    int day = 0;
    int month = 0;
    int year = 0;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_display);

        Button addTask = findViewById(R.id.addTask);

        Bundle extras = getIntent().getExtras();
        day = extras.getInt("day");
        month = extras.getInt("month");
        year = extras.getInt("year");

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(day, month, year);
            }
        });

    }

    public void changeActivity(int day, int month, int year) {
        Log.d("sdfsd", "sdf");
        Intent intent = new Intent(this, CreateTask.class);
        intent.putExtra("day", day);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        startActivity(intent);
    }


}
