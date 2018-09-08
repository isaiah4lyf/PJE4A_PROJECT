package com.example.isaia.sss_mobile_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Count_Images;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Login_Class;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private EditText User_Name;
    private EditText Password;
    private ProgressDialog progressDialog;
    private String loginString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        User_Name = (EditText)findViewById(R.id.User_Name);
        Password = (EditText)findViewById(R.id.Password);
        final Button Reg_User = (Button) findViewById(R.id.Reg_User);

        Reg_User.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        Intent intent = null;
                        try {
                            intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Reg_User"));
                            startActivity(intent);

                        } catch (ClassNotFoundException e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
        final Button Login = (Button) findViewById(R.id.Login);
        Login.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        try
                        {
                            if(!User_Name.getText().toString().equals(""))
                            {
                                if(!Password.getText().toString().equals(""))
                                {
                                    login_Asy login = new login_Asy();
                                    login.execute();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Password Text Field Empty!",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"User Name Text Field Empty!",Toast.LENGTH_LONG).show();
                            }

                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(getApplicationContext(),ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                }

        );
    }
    private class login_Asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
            progressDialog = ProgressDialog.show(Login.this,"Please wait.","Connecting..!", true);
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            try
            {
                Login_Class login = new Login_Class();
                response = login.Do_The_work(User_Name.getText().toString().replaceAll(" ",""),Password.getText().toString().replaceAll(" ",""));
            }
            catch(Exception ex)
            {
                response = "Error: "+ex.getLocalizedMessage();
            }
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            if(result.startsWith("Error:"))
            {
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            }

            if(!result.equals("false"))
            {
                DBHelper mydb = new DBHelper(getApplicationContext());
                try
                {
                    int settingsRowsVoice = mydb.Number_Of_Rows_Settings_Voice();
                    if(settingsRowsVoice == 0)
                    {
                        mydb.Insert_Settings_Voice("1","1");
                    }

                    int settingsRowsImage = mydb.Number_Of_Rows_Settings_Image();
                    if(settingsRowsImage == 0)
                    {
                        mydb.Insert_Settings_Image("1","100","1");
                    }
                }
                catch(Exception ex)
                {
                    Toast.makeText(getApplicationContext(),ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                count_Images_for_Login_asy task = new count_Images_for_Login_asy();
                task.execute(new String[]{result});
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Incorrect User Name or Password!",Toast.LENGTH_LONG).show();
            }
        }
    }
    private class count_Images_for_Login_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try
            {
                Count_Images count_class = new Count_Images();
                loginString = urls[0];
                response = count_class.Do_The_work(loginString.split(",")[0]);
            }
            catch (Exception ex)
            {
                response =  "Error: "+ex.getMessage();
            }
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            if(result.startsWith("Error:"))
            {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            }
            if(!result.equals(""))
            {
                if(Integer.parseInt(result) > 10 || Integer.parseInt(result) == 10)
                {
                    String[] userDetails = loginString.split(",");
                    Intent intent = null;
                    try {
                        DBHelper mydb = new DBHelper(getApplicationContext());
                        String insert = String.valueOf(mydb.insert_Login_State(userDetails[1],userDetails[0]));
                        intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Main_Menu"));
                        startActivity(intent);
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    String[] userDetails = loginString.split(",");
                    DBHelper mydb = new DBHelper(getApplicationContext());
                    String insert = String.valueOf(mydb.insert_Login_State(userDetails[1],userDetails[0]));
                    Intent intent = new Intent(getApplicationContext(),Main_Activity_Images.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                    int images_left = 10 - Integer.parseInt(result);
                    Toast.makeText(getApplicationContext(),images_left + " Images Left...",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
