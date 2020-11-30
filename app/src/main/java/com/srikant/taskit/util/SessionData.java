package com.srikant.taskit.util;

public class SessionData {
    static String token;
    static String name;
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
        String dueDate;
        String description;

        Task(String name, String dueDate, String description) {
            this.name = name;
            this.dueDate = dueDate;
            this.description = description;
        }
    }
}
