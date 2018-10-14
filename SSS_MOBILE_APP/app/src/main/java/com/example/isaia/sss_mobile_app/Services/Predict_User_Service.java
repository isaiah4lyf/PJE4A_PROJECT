package com.example.isaia.sss_mobile_app.Services;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.R;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Check_Accuracy;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Predict_User_Service extends Service{
    private Camera mCamera;
    private boolean capture;
    private static String User_Name;
    private static String Password;
    private static  String file_Name = "";
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;
    private int invalidPrediction;
    private DBHelper mydb;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        try {

            mydb = new DBHelper(getApplicationContext());
            User_Name = mydb.User_Name();
            Password = mydb.Password();
            invalidPrediction = 0;
            capture = true;
            devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
            activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            compName = new ComponentName(this, MyAdmin.class);

            Thread thread = new Thread() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    super.run();
                    int predict_Interval = Integer.parseInt(mydb.Get_Image_Verification_Interval());    //Already in seconds
                    while (capture == true) {
                        try {

                            boolean vn_pred_serviceRunning = false;
                            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                                if (Predict_User_Service_VN.class.getName().equals(service.service.getClassName())) {
                                    vn_pred_serviceRunning = true;
                                }
                            }
                            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                            boolean result= Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isInteractive()|| Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isScreenOn();
                            if(haveNetworkConnection() == true)
                            {
                                if(result == true && predict_Interval == 0 && vn_pred_serviceRunning == false)
                                {
                                    mCamera = getCameraInstance();
                                    sleep(3000);
                                    mCamera.takePicture(null, null,mPicture);
                                    mCamera = null;
                                    predict_Interval = Integer.parseInt(mydb.Get_Image_Verification_Interval());
                                }
                            }
                            else
                            {
                                // Take pictures for offline here and upload later when the device is online
                            }

                            if(result == false)
                            {
                                invalidPrediction = 0;
                            }
                            sleep(1000);
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
        //stopping the player when service is destroyed
        capture = false;
        stopService(new Intent(getApplicationContext(), Predict_User_Service_VN.class));
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
    //camera Code
    // A safe way to get an instance of the Camera object.
    public static Camera getCameraInstance(){
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    //Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                //Log.d(TAG, "Error creating media file, check storage permissions: " +
                //         e.getMessage());
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                file_Name = pictureFile.getName();
                Toast.makeText(getApplicationContext(),file_Name,Toast.LENGTH_LONG).show();
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            pred_user_asy tast = new pred_user_asy();
                            tast.execute();
                        }
                        catch (Exception ex)
                        {
                            //txvResult.setText(ex.toString());
                        }
                    }
                });

                thread.start();
            } catch (FileNotFoundException e) {
                //Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                //Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Create a file Uri for saving an image or video
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    // Create a File for saving an image or video
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }


    private class pred_user_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try
            {
                Pred_User pred = new Pred_User();
                response = pred.Do_The_work(User_Name,Password,file_Name);
            }
            catch (Exception ex)
            {
                response = ex.toString();
            }

            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            if(result.equals("Invalid Image") )
            {
                Toast.makeText(getApplicationContext(),String.valueOf(invalidPrediction),Toast.LENGTH_LONG).show();
                if(invalidPrediction == 5)
                {
                    int voicePredServStatus = Integer.parseInt(mydb.Get_Voice_Prediction_Service_Status());
                    if(voicePredServStatus == 1)
                    {
                        Intent serviceIntent = new Intent(getApplicationContext(),Predict_User_Service_VN.class);
                        startService(serviceIntent);
                        invalidPrediction = 0;
                    }
                }
                else
                {
                    invalidPrediction++;
                }
            }
            else
            {
                if(result.equals(User_Name))
                {
                    invalidPrediction = 0;
                }
                else
                {
                    try
                    {
                        boolean active = devicePolicyManager.isAdminActive(compName);
                        if (active) {
                            devicePolicyManager.lockNow();
                            invalidPrediction = 0;
                        }
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }
}