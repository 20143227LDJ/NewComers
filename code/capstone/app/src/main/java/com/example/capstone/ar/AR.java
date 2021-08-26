package com.example.capstone.ar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.capstone.R;

public class AR extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ar, container, false);

        Button arbtn = (Button) view.findViewById(R.id.ar_button);

        arbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UnityPlayerActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
            }
        });

        return view;
    }
}
