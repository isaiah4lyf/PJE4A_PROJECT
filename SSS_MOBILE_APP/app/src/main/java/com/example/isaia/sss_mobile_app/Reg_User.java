package com.example.isaia.sss_mobile_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_User;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;

public class Reg_User extends AppCompatActivity {

    private EditText user_Name;
    private EditText Password;
    private EditText ConfirmPassword;
    private EditText Email;
    private EditText ConfirmEmail;

    private String User_Name_String;
    private String Password_String;
    private String ConfirmPassowrd_String;
    private String Email_String;
    private String ConfirmEmail_String;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg__user);
        //Registering a user
        user_Name = (EditText) findViewById(R.id.User_Name);
        Password = (EditText) findViewById(R.id.Password);
        ConfirmPassword = (EditText) findViewById(R.id.ConfirmPassword);
        Email = (EditText) findViewById(R.id.Email);
        ConfirmEmail = (EditText) findViewById(R.id.ConfirmEmail);


        TextView sin_in = (TextView)findViewById(R.id.sign_in);
        sin_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });
        final Button reg = (Button) findViewById(R.id.Sign_Up);
        reg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try
                        {
                            User_Name_String = user_Name.getText().toString().replaceAll(" ","");
                            Password_String = Password.getText().toString().replaceAll(" ","");
                            ConfirmPassowrd_String = ConfirmPassword.getText().toString().replaceAll(" ","");
                            Email_String = Email.getText().toString().replaceAll(" ","");
                            ConfirmEmail_String = ConfirmEmail.getText().toString().replaceAll(" ","");

                            if(!User_Name_String.equals(""))
                            {
                                if(!Password_String.equals(""))
                                {
                                    if(!ConfirmPassowrd_String.equals(""))
                                    {
                                        if(!Email_String.equals(""))
                                        {
                                            if(!ConfirmEmail_String.equals(""))
                                            {
                                                if(Password_String.equals(ConfirmPassowrd_String))
                                                {

                                                    if(Email_String.equals(ConfirmEmail_String))
                                                    {
                                                        if(haveNetworkConnection() == true)
                                                        {
                                                            reg_us tast = new reg_us();
                                                            tast.execute();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(getApplicationContext(),"No Internet Connection!",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(), "Email does not match!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(), "Confirm Password text field empty!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Password text field empty!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Confirm Email text field empty!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Email text field empty!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "User name text field empty!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    private class reg_us extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
            progressDialog = ProgressDialog.show(Reg_User.this,"Please wait.","Connecting..!", true);

        }
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try
            {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wInfo = wifiManager.getConnectionInfo();
                String macAddress = wInfo.getMacAddress();
                Insert_User insert_user = new Insert_User();
                response =  insert_user.Do_The_work(User_Name_String,Email_String,Password_String,macAddress);
            }
            catch (Exception ex){
                response = ex.toString();
            }
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            progressDialog.dismiss();
            try
            {
                if(result.equals("false"))
                {
                    Toast.makeText(getApplicationContext(),"User name already taken!",Toast.LENGTH_LONG).show();
                }
                else if(result.equals("true"))
                {
                    Toast.makeText(getApplicationContext(),"Registration Successful!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
