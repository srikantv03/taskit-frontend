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

public class ListAdapter extends ArrayAdapter<SessionData.Task> {

    private int resourceLayout;
    private Context mContext;

    public ListAdapter(Context context, int resource, List<SessionData.Task> items) {
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
            v = vi.inflate(R.layout.list_item, parent, false);
        }

        SessionData.Task p = getItem(position);

        if (p != null) {
            TextView taskName = (TextView) v.findViewById(R.id.taskName);
            TextView taskDescription = (TextView) v.findViewById(R.id.taskDescription);
            TextView type = (TextView) v.findViewById(R.id.type);

                taskName.setText(p.getTaskName());
                type.setText(p.getType());
                taskDescription.setText(p.getTaskDescription());

        }

        return v;
    }

}