package com.example.isaia.sss_mobile_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User;

public class Predict_User extends AppCompatActivity implements View.OnClickListener{
    private Button buttonStart;
    private Button buttonStop;
    private String User_Name;
    private String Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict__user);


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
            Intent serviceIntent = new Intent(this,Predict_User_Service.class);
            serviceIntent.putExtra("User_Name", User_Name);
            serviceIntent.putExtra("Password", Password);
            this.startService(serviceIntent);
        } else if (v == buttonStop) {
            //stopping service
            stopService(new Intent(this, Predict_User_Service.class));
        }
    }
}
