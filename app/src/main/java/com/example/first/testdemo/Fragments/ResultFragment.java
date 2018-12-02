package com.example.first.testdemo.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.first.testdemo.R;


public class ResultFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_result, container, false);
        return view;
    }

    public void updateTextView(String message) {
        TextView text = view.findViewById(R.id.result_text);
        text.setText(message);
    }
}
