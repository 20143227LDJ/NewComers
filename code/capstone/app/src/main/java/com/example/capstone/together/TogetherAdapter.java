package com.example.capstone.together;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.ContentFrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.example.capstone.together.listview.CustomWord;
import com.example.capstone.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class TogetherAdapter extends ArrayAdapter<CustomWord> {
    public TogetherAdapter(Context context, int simpleitem, ArrayList<CustomWord> customwords) {
        super(context,0, customwords);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomWord word = getItem(position);
        //String img = "";

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.together_simpleitem, parent, false);
        }
        //ImageView image = (ImageView) convertView.findViewById(R.id.content_image);
        TextView title = (TextView) convertView.findViewById(R.id.text_view_1);
        TextView content = (TextView) convertView.findViewById(R.id.text_view_2);
        TextView party = (TextView) convertView.findViewById(R.id.text_view_3);
        TextView count = (TextView) convertView.findViewById(R.id.text_view_4);

        /*
        img = word.getImage();

        String test = "https://img1.daumcdn.net/thumb/R720x0.q80/?scode=mtistory2&fname=http%3A%2F%2Fcfs9.tistory.com%2Fimage%2F33%2Ftistory%2F2008%2F09%2F24%2F02%2F05%2F48d921e2111a4";
        Glide.with(convertView.getContext()).load(test).apply(RequestOptions.timeoutOf(5*6*1000)).into(image);
         */

        //image.setImageBitmap(word.getBmap());
        title.setText(word.getTitle());
        content.setText(word.getContents());
        party.setText(word.getParty());
        count.setText(word.getCount());

        return convertView;
    }
}
