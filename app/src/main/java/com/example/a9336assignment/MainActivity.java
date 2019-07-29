package com.example.a9336assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void task1tx(View view){
        Intent intent = new Intent(this, Task1Tx.class);
        startActivity(intent);
    }

    public void task1rx(View view){
        Intent intent = new Intent(this, Task1Rx.class);
        startActivity(intent);
    }

    public void task2tx(View view){
        Intent intent = new Intent(this, Task2Tx.class);
        startActivity(intent);
    }

    public void task2rx(View view){
            Intent intent = new Intent(this, Task2Rx.class);
        startActivity(intent);
    }

    public void task3tx(View view){
        Intent intent = new Intent(this, Task3Tx.class);
        startActivity(intent);
    }

    public void task3rx(View view){
        Intent intent = new Intent(this, Task3Rx.class);
        startActivity(intent);
    }

    public void task4tx(View view){
        Intent intent = new Intent(this, Task45Tx.class);
        startActivity(intent);
    }

    public void task4rx(View view){
        Intent intent = new Intent(this, Task45Rx.class);
        startActivity(intent);
    }
}
