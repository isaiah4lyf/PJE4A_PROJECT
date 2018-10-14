package com.example.isaia.sss_mobile_app.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Count_Images;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Get_Current_Num_Images;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Get_Device_Mac;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_Image_;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Take_Pictures_Service extends Service {
    private Camera mCamera;
    private boolean capture;
    private String User_Name;
    private String Password;
    private String file_Name;
    private boolean this_Persons_this_Device;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //User_Name = intent.getStringExtra("User_Name");
        //Password = intent.getStringExtra("Password");

        final DBHelper mydb = new DBHelper(getApplicationContext());
        User_Name = mydb.User_Name();
        Password = mydb.Password();
        capture = true;
        this_Persons_this_Device = false;

        try {

            Thread thread = new Thread() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    super.run();
                    int upload_Interval = Integer.parseInt(mydb.Get_Image_Upload_Interval())* 60;    //Converting minutes to seconds

                    while (capture == true) {
                        try {
                            if(upload_Interval == 0 && haveNetworkConnection() == true )
                            {
                                Get_Current_Num_Images num = new Get_Current_Num_Images();
                                int num_Images = Integer.parseInt(num.Do_The_work());
                                Count_Images count_Im = new Count_Images();
                                int count = Integer.parseInt(count_Im.Do_The_work(Password));
                                check_MAC check_Asy = new check_MAC();
                                check_Asy.execute();
                                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                                boolean result= Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isInteractive()|| Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isScreenOn();
                                if(result == true  && count < num_Images && this_Persons_this_Device == true)
                                {
                                    mCamera = getCameraInstance();
                                    sleep(6000);
                                    mCamera.takePicture(null, null,mPicture);
                                    mCamera = null;
                                    upload_Interval = Integer.parseInt(mydb.Get_Image_Upload_Interval())* 60;
                                }
                            }
                            sleep(1000);
                            if(upload_Interval > 0)
                            {
                                upload_Interval--;
                            }
                        } catch (Exception e) {
                            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
        capture = false;
        //mCamera = null;
    }


    //camera Code
    // A safe way to get an instance of the Camera object.
    public static Camera getCameraInstance() {
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
            if (pictureFile == null) {
                //Log.d(TAG, "Error creating media file, check storage permissions: " +
                //         e.getMessage());
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                file_Name = pictureFile.getName();
                Toast.makeText(getApplicationContext(), file_Name, Toast.LENGTH_LONG).show();

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {
                            insert_image_asy tast = new insert_image_asy();
                            tast.execute();
                        } catch (Exception ex) {
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
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    // Create a File for saving an image or video
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }

    private class check_MAC extends AsyncTask<String, Void, String> {
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
                Get_Device_Mac mac_String = new Get_Device_Mac();
                response = mac_String.Do_The_work(macAddress);
            }
            catch (Exception ex)
            {
                response =  "Error: "+ex.getMessage();
            }
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            try
            {
                if(result.split(",")[2].equals(Password))
                {
                    this_Persons_this_Device = true;
                }
                else
                {
                    this_Persons_this_Device = false;
                }
            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(),ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }

        }
    }

    private class insert_image_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }

        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp");
            Insert_Image_ client = new Insert_Image_();
            response = client.Do_The_work(User_Name, Password, mediaStorageDir.getPath() + File.separator + file_Name, "yes");


            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}
