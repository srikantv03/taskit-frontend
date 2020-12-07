package com.srikant.taskit.ui;

import android.app.AppComponentFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.srikant.taskit.R;
import com.srikant.taskit.util.SessionData;

import org.w3c.dom.Text;

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

public class CreateTask extends AppCompatActivity {

    int day;
    int month;
    int year;
    TextInputLayout name;
    TextInputLayout description;


    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.create_task);

        Button createTask = findViewById(R.id.submit);
        Button changeDate = findViewById(R.id.pickDate);
        TextView date = findViewById(R.id.date);
        description = findViewById(R.id.description);
        name = findViewById(R.id.name);

        Bundle extras = getIntent().getExtras();
        day = extras.getInt("day");
        month = extras.getInt("month");
        year = extras.getInt("year");

        createTask.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                createTask();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createTask() {
        enableStrictMode();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SessionData.getToken());
        params.put("taskName", name.getEditText().getText().toString());
        params.put("day", Integer.toString(this.day));
        params.put("month", Integer.toString(this.month));
        params.put("year", Integer.toString(this.year));
        params.put("description", description.getEditText().getText().toString());
        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0){
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }
        try{
            String url = "http://www.srikantv.com/taskitAPI/newTask";
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.connect();

            String paramsString = sbParams.toString();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();

            try {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                SessionData.getTasks();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
}
