package com.example.a9336assignment;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Task2Tx extends AppCompatActivity {
    private AudioTrack audioTrack;
    private int sampleRate;
    int duration = 3;
    int sampleSize;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task2_tx);

        sampleRate = getIntent().getIntExtra("sample rate", 0);
        sampleSize = duration * sampleRate;
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, sampleSize,
                AudioTrack.MODE_STATIC);
    }

    protected void onDestroy(){
        super.onDestroy();
        audioTrack.stop();
        audioTrack.release();
    }

    public void sendAudible(View view){
        String number = ((TextInputEditText)findViewById(R.id.number)).getText().toString();
        int freq = Integer.parseInt(number)*20 + 400;

        short samples[] = new short[sampleSize];
        for( int i=0; i<sampleSize; i++){
            samples[i] = (short)(Math.sin(2 * Math.PI * freq * i / sampleRate)*Short.MAX_VALUE);
        }
        if (count > 0){
            audioTrack.stop();
            audioTrack.reloadStaticData();
        }
        audioTrack.write(samples, 0, sampleSize);
        audioTrack.play();
        count++;
    }

    public void sendInaudible(View view){
        String number = ((TextInputEditText)findViewById(R.id.number)).getText().toString();
        int freq = Integer.parseInt(number)*300 + 16700;

        short samples[] = new short[sampleSize];
        for( int i=0; i<sampleSize; i++){
            samples[i] = (short)(Math.sin(2 * Math.PI * freq * i / sampleRate)*Short.MAX_VALUE);
        }
        if (count > 0){
            audioTrack.stop();
            audioTrack.reloadStaticData();
        }
        audioTrack.write(samples, 0, sampleSize);
        audioTrack.play();
        count++;
    }
}
