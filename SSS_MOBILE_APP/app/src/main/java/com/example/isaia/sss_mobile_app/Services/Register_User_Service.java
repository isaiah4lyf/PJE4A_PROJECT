package com.example.isaia.sss_mobile_app.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.Login;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_User;

public class Register_User_Service extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String User_Name_String;
    private String Email_String;
    private String Password_String;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DBHelper mdyb = new DBHelper(getApplicationContext());
        User_Name_String = mdyb.User_Name_String();
        Email_String = mdyb.Phone_Number();
        Password_String = mdyb.Password_String();
        reg_us reg = new reg_us();
        reg.execute();

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class reg_us extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here


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
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
