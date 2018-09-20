package com.example.isaia.sss_mobile_app.Services;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Audio_Recorder.RecordingSampler_For_Prediction;
import com.example.isaia.sss_mobile_app.Audio_Recorder.VisualizerView;
import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.R;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Train_Images_Model;


public class Train_Images_Model_Service extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String Password;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DBHelper mydb = new DBHelper(getApplicationContext());
        Password = mydb.Password();
        train_asy task = new train_asy();
        task.execute();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class train_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            try
            {
                Train_Images_Model train_images_model = new Train_Images_Model();
                response = train_images_model.Do_The_work(Password);
            }
            catch (Exception ex)
            {
                response = ex.getMessage();
            }

            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).toString();
            Intent serviceIntent = new Intent(getApplicationContext(),Predict_User_Image_Preview.class);
            startService(serviceIntent);
            stopService(new Intent(getApplicationContext(), Train_Images_Model_Service.class));
        }
    }
}
