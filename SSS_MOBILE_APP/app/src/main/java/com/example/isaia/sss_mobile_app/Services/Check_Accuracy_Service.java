package com.example.isaia.sss_mobile_app.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Check_Accuracy;

public class Check_Accuracy_Service extends Service{
    private static String User_Name;
    private static String Password;
    private static  String file_Name = "";
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;
    private int invalidPrediction;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            final DBHelper mydb = new DBHelper(getApplicationContext());
            User_Name = mydb.User_Name();
            Password = mydb.Password();
            invalidPrediction = 0;

            devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
            activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            compName = new ComponentName(this, MyAdmin.class);

            Thread thread = new Thread() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    super.run();
                    int predict_Interval = Integer.parseInt(mydb.Get_Image_Verification_Interval());    //Already in seconds
                    while (true) {
                        try {


                            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                            boolean result= Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isInteractive()|| Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isScreenOn();
                            if(result == true && predict_Interval == 0)
                            {

                                check_accuracy_Images task = new check_accuracy_Images();
                                task.execute();
                                predict_Interval = Integer.parseInt(mydb.Get_Image_Verification_Interval());
                            }

                            sleep(5000);
                            if(predict_Interval > 0)
                            {
                                predict_Interval--;
                            }
                        }
                        catch (Exception e) {
                            // Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
            };
            thread.start();
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class check_accuracy_Images extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try
            {
                Check_Accuracy count_class = new Check_Accuracy();
                DBHelper mydb = new DBHelper(getApplicationContext());
                String User_ID = mydb.Password();
                response = count_class.Do_The_work(User_ID);
            }
            catch (Exception ex)
            {
                response =  "Error: "+ex.getMessage();
            }
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {

            DBHelper mydb = new DBHelper(getApplicationContext());
            if(!result.startsWith("Error:"))
            {
                String[] accu_Tokens = result.split(",");
                if(Integer.parseInt(accu_Tokens[2]) == 0)
                {

                    if(Integer.parseInt(mydb.Get_Preview_Running_Status()) == 0)
                    {
                        Intent serviceIntent = new Intent(getApplicationContext(),Predict_User_Image_Preview.class);
                        startService(serviceIntent);
                    }
                    mydb.Update_insert_Accuracy_Management("0","1");
                }
                else
                {
                    mydb.Update_insert_Accuracy_Management("1",mydb.Get_Preview_Running_Status());
                }
            }

        }
    }
}
