package com.forevas.ap.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.forevas.ap.R;
import com.forevas.ap.engine.AudioEngine;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvRecord, tvChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvRecord = (TextView) findViewById(R.id.tv_record);
        tvChange = (TextView) findViewById(R.id.tv_change);
        tvRecord.setOnClickListener(this);
        tvChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_record:
                startActivity(new Intent(this,RecordActivity.class));
                break;
            case R.id.tv_change:
                startActivity(new Intent(this,ChangeActivity.class));
                break;
        }
    }
}
