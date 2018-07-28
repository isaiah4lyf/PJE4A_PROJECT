package com.example.isaia.sss_mobile_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_Image_;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Train_Images_Model;

import java.io.File;

public class Insert_Image extends AppCompatActivity {
    private TextView txvResult;
    private String Image_Name;
    private String User_Name;
    private String Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert__image);

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
        final Button Login = (Button) findViewById(R.id.back);
        Login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera

                    }
                }
        );
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
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp");
            txvResult.setText(mediaStorageDir.getPath() + File.separator);
            Insert_Image_ client = new Insert_Image_();
            response = client.Do_The_work(User_Name,Password,mediaStorageDir.getPath() + File.separator+Image_Name,"yes");

            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            txvResult.setText(result);

            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
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
}
