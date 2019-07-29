package com.example.a9336assignment;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import java.io.FileWriter;

public class Task1Rx extends AppCompatActivity {
    private int sampleRate;
    AudioRecord record;
    short[] audioBuffer;
    private int sampleSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task1_rx);
        sampleRate = getIntent().getIntExtra("sample rate", 0);
        sampleSize = sampleRate * 1;

        int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        System.out.println("minBufferSize "+bufferSize);

        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = sampleRate*2;
        }
        audioBuffer = new short[sampleRate];
        record = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);
        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
            System.out.println("Audio Record can't initialize!");
            return;
        }

    }

    public void record(View view){
        new Thread(){
            @Override
            public void run() {
                record.startRecording();
                int numberOfShort = 0;
                List<Short> data = new ArrayList<>();
                while (numberOfShort < audioBuffer.length) {
                    int n = record.read(audioBuffer, 0, audioBuffer.length);
                    System.out.println("buffer");
                    for (int i=10000 ;i <10100;i++) {
                        if (i%10 == 0){
                            System.out.println();
                        }
                        System.out.print(" "+audioBuffer[i]);
                    }
                    System.out.println();
                    numberOfShort += n;
                    for (int i=0; i<n; i++){
                        data.add(audioBuffer[i]);
                    }
                }
                System.out.println("Recording stopped. Samples read: "+numberOfShort);
                record.stop();
                detectFreq(data);
            }
        }.run();
    }

    public void detectFreq(List<Short> data){
        double max_magnitude = 0;
        int max_freq = 0;
        for (int freq = 200; freq <= 2000; freq += 10) {
            double m = runGoertzel(data, freq);
            if (m > max_magnitude){
                max_magnitude = m;
                max_freq = freq;
            }
        }
        ((TextView)findViewById(R.id.result)).setText("detected frequency: "+max_freq+"Hz");
    }

    public double runGoertzel(List<Short> data, int target_freq){
        int N = data.size();
        int k = (int)(0.5+1.0*N*target_freq/(double)sampleRate);
        double w = (2.0*Math.PI/N)*k;
        double cosine = Math.cos(w);
        double sine = Math.sin(w);
        double coeff = 2.0 * cosine;
        double q1=0, q2=0, q0;

        int i = 0;
        for (short sample: data){
            q0 = coeff*q1 - q2 + sample*1.0;
            q2 = q1;
            q1 = q0;
        }
        return q1*q1 + q2*q2-q1*q2*coeff;
    }

}
