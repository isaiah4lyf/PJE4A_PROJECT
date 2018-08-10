package com.example.isaia.sss_mobile_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.isaia.sss_mobile_app.Services.Take_Pictures_Service;

public class Capture_Images_Auto extends AppCompatActivity implements View.OnClickListener{

    private Button buttonStart;
    private Button buttonStop;
    private String User_Name;
    private String Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture__images__auto);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        //attaching onclicklistener to buttons
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonStart) {
            //starting service
            Intent serviceIntent = new Intent(this,Take_Pictures_Service.class);
            this.startService(serviceIntent);
        } else if (v == buttonStop) {
            //stopping service
            stopService(new Intent(this, Take_Pictures_Service.class));
        }
    }
}
