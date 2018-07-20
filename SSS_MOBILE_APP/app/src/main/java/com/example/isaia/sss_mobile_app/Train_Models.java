package com.example.isaia.sss_mobile_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Train_Images_Model;

public class Train_Models extends AppCompatActivity {
    private TextView txvResult;
    private String User_Name;
    private String Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train__models);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            User_Name = extras.getString("User_Name");
            Password = extras.getString("Password");
        }
        txvResult = (TextView) findViewById(R.id.textView);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {


                    insert_image_asy tast = new insert_image_asy();
                    tast.execute();

                }
                catch (Exception ex)
                {
                    txvResult.setText(ex.toString());
                }
            }
        });

        thread.start();
    }

    private class insert_image_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";

            //retrain the model
            Train_Images_Model train = new Train_Images_Model();
            response += " " + train.Do_The_work(Password);
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            txvResult.setText(result);
        }
    }
}
