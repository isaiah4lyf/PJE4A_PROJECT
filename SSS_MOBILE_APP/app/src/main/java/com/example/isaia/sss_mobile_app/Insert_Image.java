package com.example.isaia.sss_mobile_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_Image_;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Train_Images_Model;

public class Insert_Image extends AppCompatActivity {
    private TextView txvResult;
    private String Image_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert__image);
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

            ///Sending an image
            Insert_Image_ client = new Insert_Image_();
            response = client.Do_The_work("Isaiah","81",Image_Name);

            //retrain the model
            //Train_Images_Model train = new Train_Images_Model();
            //response += " " + train.Do_The_work("81");
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            txvResult.setText(result);
        }
    }
}
