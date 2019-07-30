package com.example.a9336assignment;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Task45Rx extends AppCompatActivity {
    private int sampleRate;
    AudioRecord record;
    short[] audioBuffer;
    public boolean stopListening;
    private int samplesPerBit = 10000;
    private int smallBlockSize = samplesPerBit/10;
    private boolean synchronizing = true;
    private int freq1 = 1000;  // frequency representing bit 1
    private int freq0 = 500; //frequency representing bit 0
    private Double magThreshold = 1E9;
    private List<Short> unusedData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task45_rx);
        sampleRate = getIntent().getIntExtra("sample rate", 0);

        int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        //System.out.println("minBufferSize "+bufferSize);

        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = sampleRate*2;
        }
        audioBuffer = new short[sampleRate*2];       // 2 second buffer
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
                int c = 0;
                while (!stopListening) {
                    int n = record.read(audioBuffer, 0, audioBuffer.length);
                    numberOfShort += n;
                    List<Short> data = new ArrayList<>();
                    for (int i=0; i<n; i++){
                        data.add(audioBuffer[i]);
                    }
                    processData(data);
                    c++;
                    if (c > 2){
                        break;
                    }
                }
                System.out.println("Recording stopped. Samples read: "+numberOfShort);
                record.stop();
            }
        }.run();
    }

    public void processData(List<Short> data){
        if(data.size() == 0){
            return;
        }
        if (synchronizing){
            synchronize(data);
        } else {
            getData(data);
        }
    }

    public void synchronize(List<Short> data){
        unusedData.addAll(data);
        data = unusedData;
        int last_small_bit = -1;
        int block = 0;
        for (block = 0; (block+1)*smallBlockSize <= data.size(); block++){
            List<Short> blockData = data.subList(block*smallBlockSize, (block+1)*smallBlockSize);
            double mag1 = runGoertzel(blockData,freq1);
            double mag0 = runGoertzel(blockData,freq0);
            System.out.print(block);
            if (mag1 > magThreshold || mag0 > magThreshold) {
                System.out.println(" " + mag1 + " " + mag0 + " " + (mag1 > mag0));
            }
        }
        if (block*smallBlockSize == data.size()){
            unusedData = new ArrayList<>();
        } else {
            unusedData = data.subList(block * smallBlockSize, data.size());
        }
    }

    public void getData(List<Short> data){

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
