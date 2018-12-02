package com.example.first.testdemo.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.first.testdemo.R;

public class ListViewDialog extends Dialog {
    private final Context mContext;
    private ListView mListView;
    public ListViewDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
        initListView();
    }
    private void initView() {
        View contentView = View.inflate(mContext, R.layout.content_dialog, null);
        mListView = (ListView) contentView.findViewById(R.id.lv);
        setContentView(contentView);
    }
    private void initListView() {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_expandable_list_item_1);
        for (int i = 0; i < 10; i++) {
            stringArrayAdapter.add("item " + i);
        }
        mListView.setAdapter(stringArrayAdapter);
    }
}
