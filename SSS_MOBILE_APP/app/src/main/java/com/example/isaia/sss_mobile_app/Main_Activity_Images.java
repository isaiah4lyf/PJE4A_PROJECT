package com.example.isaia.sss_mobile_app;

import android.content.Context;
import android.hardware.Camera;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.content.Intent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Count_Images;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Count_VNs;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_Image_;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Train_Images_Model;
import com.example.isaia.sss_mobile_app.Services.Predict_User_Image_Preview;
import com.example.isaia.sss_mobile_app.Services.Train_Images_Model_Service;


public class Main_Activity_Images extends AppCompatActivity{


    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private TextView mText;
    private SpeechRecognizer sr;
    private static final String TAG = "MyActivity";
    private TextView txvResult;
    private byte[] data_To;
    private String function;
    private String User_Name;
    private String Password;
    private String Image_Name;
    private ImageButton recordButton;
    private ImageButton homeButton;
    private ImageButton settingButton;
    private ImageButton Insert_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_images);
        function = "Insert_Image";
        DBHelper mydb = new DBHelper(getApplicationContext());
        User_Name = mydb.User_Name();
        Password = mydb.Password();
        recordButton = (ImageButton)findViewById(R.id.recordAudio);
        homeButton = (ImageButton)findViewById(R.id.home);
        settingButton = (ImageButton)findViewById(R.id.settings);
        homeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    count_Images_for_home_asy tast = new count_Images_for_home_asy();
                                    tast.execute();
                                }
                                catch (Exception ex)
                                {
                                }
                            }
                        });
                        thread.start();
                    }
                }
        );
        settingButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    count_Images_for_settings_asy tast = new count_Images_for_settings_asy();
                                    tast.execute();
                                }
                                catch (Exception ex)
                                {
                                }
                            }
                        });
                        thread.start();
                    }
                }
        );
        recordButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    count_Images_for_VN_asy tast = new count_Images_for_VN_asy();
                                    tast.execute();
                                }
                                catch (Exception ex)
                                {
                                }
                            }
                        });
                        thread.start();
                    }
                }
        );


        try
        {
            mCamera = getCameraInstance();
            mCamera.setDisplayOrientation(90);
            preview = findViewById(R.id.camera_preview);
            mPreview = new CameraPreview(this, mCamera);
            preview.addView(mPreview);
        }
        catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
        Insert_Image = (ImageButton) findViewById(R.id.Insert_Image);
        Insert_Image.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        try{
                            function = "Insert_Image";
                            mCamera.takePicture(null, null,mPicture);
                            Insert_Image.setEnabled(false);
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG);
                        }


                    }
                }
        );

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Do nothing
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
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam; // returns null if camera is unavailable
    }
    // A basic Camera preview class
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
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

                Image_Name = pictureFile.getName();
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            insert_image_asy tast = new insert_image_asy();
                            tast.execute();
                        }
                        catch (Exception ex)
                        {
                        }
                    }
                });
                thread.start();


            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
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
            Insert_Image_ client = new Insert_Image_();
            response = client.Do_The_work(User_Name,Password,mediaStorageDir.getPath() + File.separator+Image_Name,"yes");

            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            count_Images_asy tast = new count_Images_asy();
            tast.execute();


        }
    }

    private class count_Images_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            Count_Images count_class = new Count_Images();
            response = count_class.Do_The_work(Password);
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            if(Integer.parseInt(result) == 10)
            {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;

                final DBHelper mydb = new DBHelper(getApplicationContext());
                mydb.Update_Face_Recog_Settings("1","30","1","6");

                Intent serviceIntent = new Intent(getApplicationContext(),Train_Images_Model_Service.class);
                startService(serviceIntent);
                Intent intent = new Intent(getApplicationContext(),Main_Activity_Voice_Notes.class);
                startActivity(intent);

            }
            else if(Integer.parseInt(result) > 10)
            {
                Toast.makeText(getApplicationContext(),"Images greater than 10",Toast.LENGTH_LONG).show();
                //Start Prediction and Insert Image Here services here...
                mCamera.startPreview();
                Insert_Image.setEnabled(true);
            }

            else
            {
                int images_left = 10 - Integer.parseInt(result);
                Toast.makeText(getApplicationContext(),images_left + " Image(s) Left...",Toast.LENGTH_LONG).show();
                mCamera.startPreview();
                Insert_Image.setEnabled(true);
            }

        }
    }

    private class count_Images_for_settings_asy extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            Count_Images count_class = new Count_Images();
            response = count_class.Do_The_work(Password);
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            if(!result.equals(""))
            {
                if(Integer.parseInt(result) > 10 || Integer.parseInt(result) == 10)
                {
                    count_VNS_FOR_SETTINGS_asy task = new count_VNS_FOR_SETTINGS_asy();
                    task.execute();
                }
                else
                {
                    int images_left = 10 - Integer.parseInt(result);
                    Toast.makeText(getApplicationContext(),images_left + " Images Left...",Toast.LENGTH_LONG).show();
                    mCamera.startPreview();
                    Insert_Image.setEnabled(true);
                }
            }
        }
    }
    private class count_Images_for_home_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            Count_Images count_class = new Count_Images();
            response = count_class.Do_The_work(Password);
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            if(!result.equals(""))
            {
                if(Integer.parseInt(result) > 10 || Integer.parseInt(result) == 10)
                {
                    count_VNS_FOR_HOME_asy tast = new count_VNS_FOR_HOME_asy();
                    tast.execute();
                }
                else
                {
                    int images_left = 10 - Integer.parseInt(result);
                    Toast.makeText(getApplicationContext(),images_left + " Images Left...",Toast.LENGTH_LONG).show();
                    mCamera.startPreview();
                    Insert_Image.setEnabled(true);
                }
            }
        }
    }
    private class count_Images_for_VN_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            Count_Images count_class = new Count_Images();
            response = count_class.Do_The_work(Password);
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here

            if(!result.equals(""))
            {
                if(Integer.parseInt(result) > 10 || Integer.parseInt(result) == 10)
                {
                    if(mCamera != null){
                        mCamera.stopPreview();
                        mCamera.setPreviewCallback(null);
                        mCamera.release();
                        mCamera = null;
                    }
                    Intent intent = new Intent(getApplicationContext(),Main_Activity_Voice_Notes.class);
                    startActivity(intent);
                }
                else
                {
                    int images_left = 10 - Integer.parseInt(result);
                    Toast.makeText(getApplicationContext(),images_left + " Images Left...",Toast.LENGTH_LONG).show();
                    mCamera.startPreview();
                    Insert_Image.setEnabled(true);
                }
            }
        }
    }

    private class count_VNS_FOR_HOME_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            Count_VNs count_class = new Count_VNs();
            response = count_class.Do_The_work(Password);
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            if(mCamera != null){
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
            }
            if(!result.equals(""))
            {
                if(Integer.parseInt(result) > 10 || Integer.parseInt(result) == 10)
                {
                    Intent intent = new Intent(getApplicationContext(),Main_Menu.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(),Main_Activity_Voice_Notes.class);
                    startActivity(intent);
                    int images_left = 10 - Integer.parseInt(result);
                    Toast.makeText(getApplicationContext(),images_left + " Voice Notes...",Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    private class count_VNS_FOR_SETTINGS_asy extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            Count_VNs count_class = new Count_VNs();
            response = count_class.Do_The_work(Password);
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            if(mCamera != null){
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
            }
            if(!result.equals(""))
            {
                if(Integer.parseInt(result) > 10 || Integer.parseInt(result) == 10)
                {
                    Intent intent = new Intent(getApplicationContext(),Settings_With_Drawer.class);
                    startActivity(intent);
                }
                else
                {

                    Intent intent = new Intent(getApplicationContext(),Main_Activity_Voice_Notes.class);
                    startActivity(intent);
                    int images_left = 10 - Integer.parseInt(result);
                    Toast.makeText(getApplicationContext(),images_left + " Voice Notes...",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
