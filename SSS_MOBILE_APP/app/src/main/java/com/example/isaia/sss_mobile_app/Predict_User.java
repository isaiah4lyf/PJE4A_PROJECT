package com.example.isaia.sss_mobile_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User;

public class Predict_User extends AppCompatActivity {
    private TextView txvResult;
    private String Image_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict__user);
        txvResult = (TextView) findViewById(R.id.textView);
        Bundle extras = getIntent().getExtras();
        Image_Name = "";

        if (extras != null) {
            Image_Name = extras.getString("Image_Name");

        }

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                //txvResult.setText("Hello");

                try {
                    //Registering a user
                    //Insert_User insert_user = new Insert_User();
                    //txvResult.setText(insert_user.Do_The_work("CHRIS"));

                    ///Sending an image
                    //Insert_Image client = new Insert_Image();
                    //txvResult.setText(client.Do_The_work("CHRIS","82"));



                    //retrain the model
                    //Train_Images_Model train = new Train_Images_Model();
                    //train.Do_The_work("82");

                    pred_user_asy tast = new pred_user_asy();
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
    private class pred_user_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            Pred_User pred = new Pred_User();
            return pred.Do_The_work("CHRIS","82",Image_Name);
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            txvResult.setText(result);
        }
    }
}
