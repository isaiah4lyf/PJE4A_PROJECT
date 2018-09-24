package com.example.isaia.sss_mobile_app.Services;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.Login;
import com.example.isaia.sss_mobile_app.R;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Count_VNs;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User_Image_Preview_Test_Accu;
import com.example.isaia.sss_mobile_app.Settings_With_Drawer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.view.ViewGroup.LayoutParams;


public class Predict_User_Image_Preview extends Service{
    private Camera mCamera;
    private CameraPreview mPreview;
    private RelativeLayout preview;
    private boolean capture;
    private static String User_Name;
    private static String Password;
    private static  String file_Name = "";
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;
    private int invalidPrediction;
    private Dialog dialog;


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
        dialog = new Dialog(getApplicationContext(),R.style.test_pred_images_dialog);
        dialog.setContentView(R.layout.image_prediction_pop_up);
        dialog.setTitle("S.S.S: Prediction Accuracy Testing");

        try {

            mCamera = getCameraInstance();
            mCamera.setDisplayOrientation(90);
            preview = dialog.findViewById(R.id.camera_preview);
            mPreview = new CameraPreview(this, mCamera);
            preview.addView(mPreview);
        }
        catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }

        final ImageButton takePic = dialog.findViewById(R.id.Insert_Image);
        takePic.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        try
                        {
                            mCamera.takePicture(null, null,mPicture);
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    stopService(new Intent(getApplicationContext(), Predict_User_Image_Preview.class));
                }
                return true;
            }
        });
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.0f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
        if(mCamera != null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
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
                //Log.d(TAG, "Error setting camera preview: " + e.getMessage());
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
               // Log.d(TAG, "Error starting camera preview: " + e.getMessage());
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
            Pred_User_Image_Preview_Test_Accu pred = new Pred_User_Image_Preview_Test_Accu();
            response = pred.Do_The_work(User_Name, Password, file_Name);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here

            if(result.equals("Invalid Image"))
            {
                mCamera.startPreview();
                Toast.makeText(getApplicationContext(),"Invalid Image, Test again.",Toast.LENGTH_LONG).show();
            }
            else if(result.equals("Incorrect User"))
            {
                mCamera.startPreview();
                Toast.makeText(getApplicationContext(),"Incorrect User, Test again.",Toast.LENGTH_LONG).show();
            }
            else
            {
                final Dialog dialog2 = new Dialog(getApplicationContext(),R.style.test_pred_images_dialog);
                dialog2.setContentView(R.layout.confirm_accuracy_dialog);
                dialog2.setTitle("S.S.S: Confirm Accuracy");
                final TextView data = dialog2.findViewById(R.id.data);
                data.setText(result);
                final TextView test_again = dialog2.findViewById(R.id.test_again);
                test_again.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // get an image from the camera
                                mCamera.startPreview();
                                dialog2.dismiss();
                            }
                        }
                );
                final TextView close = dialog2.findViewById(R.id.close);
                close.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // get an image from the camera
                                dialog2.dismiss();
                                dialog.dismiss();
                                stopService(new Intent(getApplicationContext(), Predict_User_Image_Preview.class));
                                count_VNS_FOR_SETTINGS_asy task = new count_VNS_FOR_SETTINGS_asy();
                                task.execute();
                            }
                        }
                );
                dialog2.setOnKeyListener(new Dialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog2.dismiss();
                            dialog.dismiss();
                            stopService(new Intent(getApplicationContext(), Predict_User_Image_Preview.class));
                            count_VNS_FOR_SETTINGS_asy task = new count_VNS_FOR_SETTINGS_asy();
                            task.execute();
                        }
                        return true;
                    }
                });
                dialog2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                WindowManager.LayoutParams lp = dialog2.getWindow().getAttributes();
                lp.dimAmount=0.0f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
                dialog2.getWindow().setAttributes(lp);
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.show();
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
            try
            {
                DBHelper mydb = new DBHelper(getApplicationContext());
                String Password = mydb.Password();
                Count_VNs count_class = new Count_VNs();
                response = count_class.Do_The_work(Password);
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
            if(!result.equals(""))
            {
                if(Integer.parseInt(result) >= 10)
                {
                    try
                    {
                        Intent intent = new Intent(getApplicationContext(),Settings_With_Drawer.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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
