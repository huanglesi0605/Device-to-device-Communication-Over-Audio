package com.example.a9336assignment;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Task45Tx extends AppCompatActivity {
    private int freq1 = 1209;  // frequency representing bit 1
    private int freq0 = 697; //frequency representing bit 0
    private int sampleRate;
    private int samplesPerBit = 10000;
    private int packetSize = 5;
    private AudioTrack audioTrack;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task45_tx);
        sampleRate = getIntent().getIntExtra("sample rate", 0);
        int sampleSize = 8 * packetSize * samplesPerBit;

        System.out.println(""+sampleRate+" "+sampleSize);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, sampleSize,
                AudioTrack.MODE_STREAM);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            audioTrack.stop();
            audioTrack.release();
        } catch (Exception e){

        }
    }

    public void onSendButtonClick(View view){
        //System.out.println("aaa");
        /*
        byte preamble = (byte)0b10101011;
        byte messageLength = (byte)10;
        byte[] bytes = new byte[]{preamble, messageLength};
        */
        String text = ((TextInputEditText)findViewById(R.id.message_sent)).getText().toString();
        send(createPacket(text));
    }

    public byte[] createPacket(String text){
        int messageLength = text.length();
        byte[] bytes = new byte[messageLength+2];
        bytes[0] = (byte)0b10101011;
        bytes[1] = (byte)messageLength;
        for (int i=0; i<messageLength; i++){
            bytes[i+2] = (byte)text.charAt(i);
        }
        return bytes;
    }

    public void send(byte[] bytes){
        //System.out.println("bbb");
        int sampleSize = 8 * bytes.length * samplesPerBit;
        short[] samples = createSamples(bytes);
        if (count > 0){
            audioTrack.stop();
            audioTrack.reloadStaticData();
        }
        System.out.println("samples length: "+samples.length);
        System.out.println("sampleSize: "+sampleSize);
        audioTrack.play();
        audioTrack.write(samples, 0, sampleSize);
        count++;
    }

    public short[] createSamples(byte[] bytes){
        //System.out.println("ccc");
        short[] samples = new short[8 * bytes.length * samplesPerBit];
        for (int i=0; i< bytes.length; i++){
            int[] binary = binarify(bytes[i]);
            int freq = 0;
            for (int j=0; j<8; j++){
                if (binary[j] == 1){
                    freq = freq1;
                } else if (binary[j] == 0){
                    freq = freq0;
                }
                for (int k=0; k < samplesPerBit; k++) {
                    samples[i*8*samplesPerBit + j*samplesPerBit + k] = (short) (Math.sin(2 * Math.PI * freq * k / sampleRate) * Short.MAX_VALUE);
                }
            }
        }
        return samples;
    }

    public static int[] binarify( byte ByteToCheck ) {
        //System.out.println("ddd");
        int[] binaryCode = new int[8];
        byte[] reference = new byte[]{ (byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };

        for ( byte z = 0; z < 8; z++ ) {
            //if bit z of byte a is set, append a 1 to binaryCode. Otherwise, append a 0 to binaryCode
            if ( ( reference[z] & ByteToCheck ) != 0 ) {
                binaryCode[z] = 1;
            }
            else {
                binaryCode[z] = 0;
            }
        }
        //System.out.println("eee");
        for (int i=0; i<8; i++){
            //System.out.println("fff");
            System.out.println(binaryCode[i]);
        }
        return binaryCode;
    }
}
