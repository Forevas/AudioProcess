package com.forevas.ap.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.forevas.ap.R;
import com.forevas.ap.engine.AudioEngine;

import java.util.concurrent.Executors;

/**
 * Created by zhuchenchen on 2017/9/20 0020.
 */

public class RecordActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tvRecord, tvPlay;
    AudioEngine audioEngine;
    boolean isRecording;
    boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        tvRecord = (TextView) findViewById(R.id.tv_record);
        tvPlay = (TextView) findViewById(R.id.tv_play);
        tvRecord.setOnClickListener(this);
        tvPlay.setOnClickListener(this);
        tvPlay.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_record:
                if (!isRecording) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            audioEngine = new AudioEngine();
                            audioEngine.startRecord();
                        }
                    });
                    isRecording = true;
                    tvRecord.setText("停止录制");
                    tvPlay.setClickable(false);
                } else {
                    audioEngine.stopRecord();
                    audioEngine.release();
                    isRecording = false;
                    tvRecord.setText("开始录制");
                    tvPlay.setClickable(true);
                }
                break;
            case R.id.tv_play:
                if (!isPlaying) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            audioEngine.playRecord();
                        }
                    });
                    tvPlay.setText("停止播放");
                    tvRecord.setClickable(false);
                    isPlaying=true;
                } else {
                    audioEngine.stopPlaying();
                    tvPlay.setText("开始播放");
                    tvRecord.setClickable(true);
                    isPlaying=false;
                }
                break;
        }
    }
}
