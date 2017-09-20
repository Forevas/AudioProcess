package com.forevas.ap.engine;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhuchenchen on 2017/9/19 0019.
 */

public class AudioEngine {
    private int sampleRate = 44100;   //音频采样率
    private int channelConfig = AudioFormat.CHANNEL_IN_STEREO;   //音频录制通道,默认为立体声
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT; //音频录制格式，默认为PCM16Bit
    private int bufferSize;
    private AudioRecord mRecorder;   //录音器
    private File file;
    private boolean isRecording;
    private boolean isPlaying;

    //开始录音
    public void startRecord() {
        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);//设置bufferSize为AudioRecord所需最小bufferSize的两倍 15360
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig,
                audioFormat, bufferSize);//初始化录音器
        mRecorder.startRecording();
        //生成PCM文件
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/demo.pcm");
        //如果存在，就先删除再创建
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("未能创建" + file.toString());
        }
        try {
            //输出流
            OutputStream os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);

            int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat) * 2;//设置bufferSize为AudioRecord所需最小bufferSize的两倍
            short[] buffer = new short[bufferSize];
            isRecording = true;
            while (isRecording) {
                int bufferReadResult = mRecorder.read(buffer, 0, bufferSize);
                for (int i = 0; i < bufferReadResult; i++) {
                    dos.writeShort(buffer[i]);
                }
            }
            mRecorder.stop();
            dos.close();
        } catch (Throwable t) {
        }
    }

    public void stopRecord() {
        isRecording = false;
    }

    //播放文件
    public void playRecord() {
        isPlaying = true;
        if (file == null) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/demo.pcm");
        }
        //读取文件
        int musicLength = (int) (file.length() / 2);
        short[] music = new short[musicLength];
        try {
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRate, channelConfig,
                    AudioFormat.ENCODING_PCM_16BIT,
                    (int) file.length(),
                    AudioTrack.MODE_STREAM);
            audioTrack.play();
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            int i = 0;
            while (dis.available() > 0) {
                music[i] = dis.readShort();
                i++;
            }
            audioTrack.write(music, 0, musicLength);
            audioTrack.stop();
            dis.close();
        } catch (Exception e) {
        }
    }

    public void stopPlaying() {
        isPlaying = false;
    }

    public void release() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }
}
