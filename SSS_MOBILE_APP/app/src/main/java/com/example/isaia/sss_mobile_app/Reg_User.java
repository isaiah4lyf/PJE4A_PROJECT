package com.example.isaia.sss_mobile_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_User;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User;

public class Reg_User extends AppCompatActivity {

    private EditText user_Name;
    private EditText Password;
    private String User_Name_String;
    private String Password_String;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg__user);
        //Registering a user
        user_Name = (EditText) findViewById(R.id.User_Name);
        Password = (EditText) findViewById(R.id.Password);


        final Button reg = (Button) findViewById(R.id.Sign_Up);
        reg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User_Name_String = user_Name.getText().toString();
                        Password_String = Password.getText().toString();
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    reg_us tast = new reg_us();
                                    tast.execute();

                                }
                                catch (Exception ex)
                                {
                                    //txvResult.setText(ex.toString());
                                }
                            }
                        });

                        thread.start();
                    }
                }
        );
    }
    private class reg_us extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            Insert_User insert_user = new Insert_User();
            return insert_user.Do_The_work(User_Name_String);
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            //txvResult.setText(result);
        }
    }

}
