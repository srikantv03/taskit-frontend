package com.srikant.taskit.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.srikant.taskit.ClassroomQuickstart;
import com.srikant.taskit.R;
import com.srikant.taskit.ui.DueTodayAdapter;
import com.srikant.taskit.ui.ListAdapter;
import com.srikant.taskit.ui.TaskView;
import com.srikant.taskit.util.SessionData;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //js
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        CalendarView calendarView = root.findViewById(R.id.calendarView);
        final ListView listView = root.findViewById(R.id.dueToday);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                launchTaskView(i, i1, i2);
            }
        });


        Date date = new Date(); // your date

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        cal.setTime(date);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        ArrayList<SessionData.Task> viewTasks = SessionData.getForDMY(day, month, year);
        DueTodayAdapter adapter = new DueTodayAdapter(getContext(), 0, viewTasks);

        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch(position) {
                    case 0:
                        ArrayList<SessionData.Task> viewTasks = SessionData.getForDMY(day, month, year);
                        DueTodayAdapter adapter = new DueTodayAdapter(getContext(), 0, viewTasks);
                        listView.setAdapter(adapter);
                    case 1:
                        SessionData.sortTasks();
                        ArrayList<SessionData.Task> getTop = SessionData.getTasks(day, month, year);
                        DueTodayAdapter topAdapter = new DueTodayAdapter(getContext(), 0, getTop);
                        listView.setAdapter(topAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ArrayAdapter<CharSequence> sequence = ArrayAdapter.createFromResource(getContext(),
                R.array.home_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(sequence);



        return root;
    }

    private void launchTaskView(int y, int m, int d) {


        Intent intent = new Intent(getActivity(), TaskView.class);
        intent.putExtra("day", d);
        intent.putExtra("month", m);
        intent.putExtra("year", y);
        startActivity(intent);
    }
}