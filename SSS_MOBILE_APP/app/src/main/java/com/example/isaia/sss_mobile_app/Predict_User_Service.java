package com.example.isaia.sss_mobile_app;

import android.app.ActivityManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import com.example.isaia.sss_mobile_app.Database.DBHelper;
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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DBHelper mydb = new DBHelper(getApplicationContext());
        User_Name = mydb.User_Name();
        Password = mydb.Password();

        capture = true;
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);
        try {

            Thread thread = new Thread() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    super.run();
                    while (capture == true) {
                        try {
                            mCamera = getCameraInstance();
                            mCamera.takePicture(null, null,mPicture);
                            mCamera = null;
                            sleep(20000);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Log.d("TEST", e.getMessage());
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
        //mCamera = null;
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
            Pred_User pred = new Pred_User();
            response = pred.Do_The_work(User_Name,Password,file_Name);
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            if(result.equals("Invalid Image"))
            {
                boolean active = devicePolicyManager.isAdminActive(compName);
                if (active) {
                    devicePolicyManager.lockNow();
                } else {
                    Toast.makeText(getApplicationContext(), "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
                    //startActivityForResult(intent, RESULT_ENABLE);
                }
            }
            else
            {
                //Do some things here
            }
        }
    }

}