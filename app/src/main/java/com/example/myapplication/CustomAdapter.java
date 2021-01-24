package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomAdapter extends BaseAdapter {
    Context context;
    JSONArray listDevices;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, JSONArray listDevices) {
        this.context = context;
        this.listDevices = listDevices;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listDevices.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView name = (TextView) view.findViewById(R.id.textViewItem);
        TextView code = (TextView) view.findViewById(R.id.textViewNote);
        JSONObject device = null;
        try {
            device = (JSONObject) listDevices.get(i);
            System.out.println("Show device "+device.toString());
            name.setText(device.get("device_name").toString());
            code.setText(device.get("device_code").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
