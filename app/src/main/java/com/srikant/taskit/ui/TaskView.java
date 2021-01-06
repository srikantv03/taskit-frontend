package com.srikant.taskit.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.srikant.taskit.MainActivity;
import com.srikant.taskit.R;
import com.srikant.taskit.ui.home.HomeFragment;
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
import java.util.ArrayList;
import java.util.HashMap;

public class TaskView extends AppCompatActivity {
    int day = 0;
    int month = 0;
    int year = 0;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_display);
        SessionData.getTasks();

        // Construct the data source
        FloatingActionButton back = findViewById(R.id.back);

        Button addTask = findViewById(R.id.addTask);

        Bundle extras = getIntent().getExtras();
        TextView title = findViewById(R.id.title);
        day = extras.getInt("day");
        month = extras.getInt("month");
        year = extras.getInt("year");
        ArrayList<SessionData.Task> viewTasks = new ArrayList<>();
        viewTasks = SessionData.getForDMY(day, month, year);

        ListAdapter adapter = new ListAdapter(this, 0, viewTasks);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        title.setText("TASKS FOR " + (month + 1) + "/" + day + "/" + year);


        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(day, month, year);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

    }

    public void changeActivity(int day, int month, int year) {
        Intent intent = new Intent(this, CreateTask.class);
        intent.putExtra("day", day);
        intent.putExtra("month", month);
        intent.putExtra("year", year);
        startActivity(intent);
    }

    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
