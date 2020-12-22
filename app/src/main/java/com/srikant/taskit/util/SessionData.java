package com.srikant.taskit.util;

import android.graphics.Canvas;
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
    static ArrayList<Canvas> canvasTokens;

    public static void setName(String n) {
        name = n;
    }
    public static void setToken(String t) {
        token = t;
    }

    public static String getToken() {
        return token;
    }

    public static ArrayList<Canvas> getAllCanvas() {
        return canvasTokens;
    }

    public static ArrayList<Task> getForDMY(int day, int month, int year) {
        ArrayList<Task> returnList = new ArrayList<Task>();
        if(tasks != null) {
            for (int i = 0; i < tasks.size(); i++) {
                Task temp = tasks.get(i);
                if (temp.getDay() == day && temp.getMonth() == month && temp.getYear() == year) {
                    returnList.add(temp);
                }
            }
        }
        return returnList;
    }

    public static class Canvas {
        String name;
        String domain;
        String token;
        String beforeSetting;
        String afterSetting;

        public Canvas(String name, String token, String domain, String b, String a) {
            this.name = name;
            this.token = token;
            this.domain = domain;

            this.beforeSetting = b;
            this.afterSetting = a;
        }

        public String getName() {
            return name;
        }
        public String getToken() {
            return token;
        }
        public String getDomain() {return domain;}
    }


    public static class Task {
        String name;
        int day;
        int month;
        int year;
        String description;

        public Task(String name, String description, int day, int month, int year) {
            this.name = name;
            this.day = day;
            this.month = month;
            this.year = year;
            this.description = description;
        }

        public int getDay() {
            return day;
        }
        public int getMonth() {
            return month;
        }
        public int getYear() {
            return year;
        }
        public String getTaskName() {
            return name;
        }
        public String getTaskDescription() {
            return description;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void getTasks() {
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
                JSONArray arr = obj.getJSONArray("tasks");
                for(int j = 0; j < arr.length(); j++) {
                    JSONObject object = arr.getJSONObject(j);
                    String taskName = object.getString("taskName");
                    int day = Integer.parseInt(object.getString("day"));
                    int month = Integer.parseInt(object.getString("month"));
                    int year = Integer.parseInt(object.getString("year"));
                    String description = object.getString("description");
                    Task temp = new Task(taskName, description, day, month, year);
                    Log.d("asdf", taskName);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void fromCanvasToTask() {
        for(Canvas canvas : canvasTokens) {
            String token = canvas.getToken();
            String domain = canvas.getDomain();
            try{
                String url = "http://www.srikantv.com/taskitAPI/getCanvasTasks?apiToken=" + token + "&domain=" + domain;
                URL urlObj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.connect();

                try {
                    canvasTokens = new ArrayList<>();

                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    String resultStr = result.toString();
                    JSONObject obj = new JSONObject(resultStr);
                    JSONArray arr =obj.getJSONArray("ids");


                    for (int j = 0; j < arr.length(); j++) {
                            JSONObject object = arr.getJSONObject(j);

                            String name = object.getString("name");
                            String description = "";
                            int day = Integer.parseInt(object.getString("day"));
                            int month = Integer.parseInt(object.getString("month"));
                            int year = Integer.parseInt(object.getString("year"));

                            createTaskFromCanvas(name, description, day, month, year);
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


    }

    public static void getCanvasTokens() {
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
            String url = "http://www.srikantv.com/taskitAPI/getCanvasTokens";
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
                canvasTokens = new ArrayList<>();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                String resultStr = result.toString();
                JSONObject obj = new JSONObject(resultStr);
                JSONArray arr = obj.getJSONArray("tokens");


                if(obj.getString("status").equals("1")) {
                    for (int j = 0; j < arr.length(); j++) {
                        JSONObject object = arr.getJSONObject(j);
                        String name = object.getString("name");
                        String token = object.getString("token");
                        String domain = object.getString("domain");
                        String b = object.getString("beforeCreate");
                        String a = object.getString("afterCreate");
                        Canvas canvas = new Canvas(name, token, domain, b, a);
                        canvasTokens.add(canvas);
                    }
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void createTaskFromCanvas(String name, String description, int day, int month, int year) {
        enableStrictMode();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("taskName", name);
        params.put("day", Integer.toString(day));
        params.put("month", Integer.toString(month));
        params.put("year", Integer.toString(year));
        params.put("description", description);
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

                getTasks();

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

}
