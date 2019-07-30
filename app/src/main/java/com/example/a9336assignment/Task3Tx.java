package com.example.a9336assignment;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Task3Tx extends AppCompatActivity {
    private AudioTrack audioTrack;
    private int sampleRate;
    int duration = 3;
    int sampleSize;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3_tx);

        sampleRate = getIntent().getIntExtra("sample rate", 0);
        sampleSize = duration * sampleRate;
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, sampleSize,
                AudioTrack.MODE_STATIC);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        audioTrack.stop();
        audioTrack.release();
    }

    public void send(View view){
        String digit_str = ((TextInputEditText)findViewById(R.id.dual_tone_input)).getText().toString();
        int digit = Integer.valueOf(digit_str);
        int freq1=0, freq2=0;
        switch ((digit-1)/3){
            case 0:
                freq1 = 697;
                break;
            case 1:
                freq1 = 770;
                break;
            case 2:
                freq1 = 852;
                break;
        }
        switch (digit%3){
            case 1:
                freq2 = 1209;
                break;
            case 2:
                freq2 = 1336;
                break;
            case 0:
                freq2 = 1477;
                break;
        }

        short samples[] = new short[sampleSize];
        for( int i=0; i<sampleSize; i++){
            samples[i] = (short)(Math.sin(2 * Math.PI * freq1 * i / sampleRate)/1.5*Short.MAX_VALUE);
            samples[i] += (short)(0.5*Math.sin(2 * Math.PI * freq1 * i / sampleRate)/1.5*Short.MAX_VALUE);
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
