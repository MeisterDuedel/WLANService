package com.example.wlanservice;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //private Service m_service;
   Button startButton;
   Button stoppButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.i("WlanService","Activity onCreate");

        startButton = (Button) findViewById(R.id.startButton);
        stoppButton = (Button) findViewById(R.id.stoppButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),WlanService.class);
                startService(intent);
            }
        });

        stoppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WlanService.class);
                stopService(intent);
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //Log.i("WlanService","Activity onDestroy");
    }
}
