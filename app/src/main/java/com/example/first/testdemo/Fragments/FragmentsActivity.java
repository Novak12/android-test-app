package com.example.first.testdemo.Fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.first.testdemo.R;

public class FragmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);

        setEditFragment();
        Button bt = findViewById(R.id.btn_confirm1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowResultFragment(view);
            }
        });
        LinearLayout lbt = findViewById(R.id.btn_group2);
        lbt.setVisibility(View.INVISIBLE);

        Button bt1 = findViewById(R.id.btn_confirm2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditFragment();
            }
        });
    }

    public void onShowResultFragment(View v) {
        setResultFragment();
    }

    public void onShowEditTextFragment(View view) {
        setEditFragment();
    }

    public void setEditFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments, new EditTextFragment());
        fragmentTransaction.commit();
        supportFragmentManager.executePendingTransactions();

        LinearLayout btl = findViewById(R.id.btn_group2);
        btl.setVisibility(View.INVISIBLE);

        LinearLayout bt2 = findViewById(R.id.btn_group1);
        bt2.setVisibility(View.VISIBLE);
    }

    public void setResultFragment() {
        try {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            ResultFragment resultFragment = new ResultFragment();
            fragmentTransaction.replace(R.id.fragments, resultFragment);
            fragmentTransaction.commit();
            supportFragmentManager.executePendingTransactions();

            resultFragment.updateTextView("操作成功");
            LinearLayout btl = findViewById(R.id.btn_group1);
            btl.setVisibility(View.INVISIBLE);

            LinearLayout bt2 = findViewById(R.id.btn_group2);
            bt2.setVisibility(View.VISIBLE);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onShowDialog(View v){
        ListViewDialog listDialog=new ListViewDialog(this);
        listDialog.show();
    }
}
