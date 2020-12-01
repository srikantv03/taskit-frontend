package com.srikant.taskit.util;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class SessionData {
    static String token;
    static String name;
    static ArrayList<Task> tasks;

    public static void setName(String n) {
        name = n;
    }
    public static void setToken(String t) {
        token = t;
    }

    public static String getToken() {
        return token;
    }

    class Task {
        String name;
        int day;
        int month;
        int year;
        String description;

        Task(String name, String description, int day, int month, int year) {
            this.name = name;
            this.day = day;
            this.month = month;
            this.year = year;
            this.description = description;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getTasks() {
        enableStrictMode();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
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
            String url = "http://www.srikantv.com/taskitAPI/getTasks";
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
                tasks = new ArrayList<>();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                String resultStr = result.toString();
                JSONObject obj = new JSONObject(resultStr);
                JSONArray arr = new JSONArray(obj.getJSONArray("tasks"));

                for(int j = 0; j < arr.length(); j++) {
                    JSONObject object = arr.getJSONObject(j);
                    String taskName = object.getString("taskName");
                    int day = Integer.parseInt(object.getString("day"));
                    int month = Integer.parseInt(object.getString("month"));
                    int year = Integer.parseInt(object.getString("year"));
                    String description = object.getString("description");
                    Task temp = new Task(taskName, description, day, month, year);
                    tasks.add(temp);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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

    public static void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

}
