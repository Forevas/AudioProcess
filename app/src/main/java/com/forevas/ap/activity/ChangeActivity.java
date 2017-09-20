package com.forevas.ap.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forevas.ap.R;
import com.forevas.ap.engine.Utils;

import org.fmod.FMOD;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhuchenchen on 2017/9/20 0020.
 */

public class ChangeActivity extends AppCompatActivity implements View.OnClickListener{
    private ExecutorService fixedThreadPool;
    private PlayerThread playerThread;
    private String path = "file:///android_asset/demo.mp3";
    private int type;
    private TextView normal, luoli, dashu, jingsong, gaoguai, kongling;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        normal = (TextView) findViewById(R.id.tv_change_1);
        luoli = (TextView) findViewById(R.id.tv_change_2);
        dashu = (TextView) findViewById(R.id.tv_change_3);
        jingsong = (TextView) findViewById(R.id.tv_change_4);
        gaoguai = (TextView) findViewById(R.id.tv_change_5);
        kongling = (TextView) findViewById(R.id.tv_change_6);
        normal.setOnClickListener(this);
        luoli.setOnClickListener(this);
        dashu.setOnClickListener(this);
        jingsong.setOnClickListener(this);
        gaoguai.setOnClickListener(this);
        kongling.setOnClickListener(this);
        fixedThreadPool = Executors.newFixedThreadPool(1);
        FMOD.init(this);
    }
    class PlayerThread implements Runnable {
        @Override
        public void run() {
            Utils.fix(path, type);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_1:
                type = Utils.MODE_NORMAL;
                break;
            case R.id.tv_change_2:
                type = Utils.MODE_LUOLI;
                break;
            case R.id.tv_change_3:
                type = Utils.MODE_DASHU;
                break;
            case R.id.tv_change_4:
                type = Utils.MODE_JINGSONG;
                break;
            case R.id.tv_change_5:
                type = Utils.MODE_GAOGUAI;
                break;
            case R.id.tv_change_6:
                type = Utils.MODE_KONGLING;
                break;
        }
        playerThread = new PlayerThread();
        fixedThreadPool.execute(playerThread);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FMOD.close();
    }
}
