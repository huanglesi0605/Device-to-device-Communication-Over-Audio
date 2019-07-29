package com.example.a9336assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private int sampleRate = 20000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void task1tx(View view){
        Intent intent = new Intent(this, Task1Tx.class);
        intent.putExtra("sample rate", sampleRate);
        startActivity(intent);
    }

    public void task1rx(View view){
        Intent intent = new Intent(this, Task1Rx.class);
        intent.putExtra("sample rate", sampleRate);
        startActivity(intent);
    }

    public void task2tx(View view){
        Intent intent = new Intent(this, Task2Tx.class);
        intent.putExtra("sample rate", sampleRate);
        startActivity(intent);
    }

    public void task2rx(View view){
        Intent intent = new Intent(this, Task2Rx.class);
        intent.putExtra("sample rate", sampleRate);
        startActivity(intent);
    }

    public void task3tx(View view){
        Intent intent = new Intent(this, Task3Tx.class);
        intent.putExtra("sample rate", sampleRate);
        startActivity(intent);
    }

    public void task3rx(View view){
        Intent intent = new Intent(this, Task3Rx.class);
        intent.putExtra("sample rate", sampleRate);
        startActivity(intent);
    }

    public void task4tx(View view){
        Intent intent = new Intent(this, Task45Tx.class);
        intent.putExtra("sample rate", sampleRate);
        startActivity(intent);
    }

    public void task4rx(View view){
        Intent intent = new Intent(this, Task45Rx.class);
        intent.putExtra("sample rate", sampleRate);
        startActivity(intent);
    }
}
