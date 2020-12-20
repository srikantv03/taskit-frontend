package com.srikant.taskit.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.srikant.taskit.R;
import com.srikant.taskit.util.SessionData;

import java.util.List;

public class CanvasListAdapter extends ArrayAdapter<SessionData.Canvas> {

    private int resourceLayout;
    private Context mContext;


    public CanvasListAdapter(Context context, int resource, List<SessionData.Canvas> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.today_task, parent, false);
        }

        SessionData.Canvas p = getItem(position);

        if (p != null) {
            TextView taskName = (TextView) v.findViewById(R.id.nameField);
            taskName.setText(p.getName());

        }

        return v;
    }
}
