package com.example.isaia.sss_mobile_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User;

public class Predict_User extends AppCompatActivity {
    private TextView txvResult;
    private String Image_Name;
    private String User_Name;
    private String Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict__user);
        txvResult = (TextView) findViewById(R.id.textView);
        Bundle extras = getIntent().getExtras();
        Image_Name = "";

        if (extras != null) {
            Image_Name = extras.getString("Image_Name");
            User_Name = extras.getString("User_Name");
            Password = extras.getString("Password");
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
                    //Pred_User pred = new Pred_User();
                    //pred.Do_The_work("CHRIS","82",Image_Name);

                }
                catch (Exception ex)
                {
                    txvResult.setText(ex.toString());
                }
            }
        });

        thread.start();
        final Button Login = (Button) findViewById(R.id.back);
        Login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        Intent intent = null;
                        try {

                            intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Main_Activity"));
                            intent.putExtra("User_Name",User_Name);
                            intent.putExtra("Password",Password);
                            startActivity(intent);

                        } catch (ClassNotFoundException e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
    private class pred_user_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            Pred_User pred = new Pred_User();
            return pred.Do_The_work(User_Name,Password,Image_Name);
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            txvResult.setText(result);
        }
    }
}
