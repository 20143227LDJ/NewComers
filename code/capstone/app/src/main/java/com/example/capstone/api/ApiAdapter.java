package com.example.capstone.api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.capstone.R;

import java.util.ArrayList;

public class ApiAdapter extends ArrayAdapter<ApiWord> {
    public ApiAdapter(Context context, int simpleitem, ArrayList<ApiWord> apiwords) {
        super(context,0, apiwords);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ApiWord word = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.api_simpleitem, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.api_text_view_1);
        TextView content = (TextView) convertView.findViewById(R.id.api_text_view_2);
        TextView location = (TextView) convertView.findViewById(R.id.api_text_view_3);

        title.setText(word.getTitle());
        content.setText(word.getContents());
        location.setText(word.getLocation());

        return convertView;
    }
}
