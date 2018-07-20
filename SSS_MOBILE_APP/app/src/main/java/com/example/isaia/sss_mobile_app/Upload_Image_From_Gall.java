package com.example.isaia.sss_mobile_app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_Image_;

import java.io.File;

public class Upload_Image_From_Gall extends AppCompatActivity {
    private Uri currImageURI;
    private TextView txvResult;
    private String User_Name;
    private String Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__image__from__gall);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
        txvResult = (TextView) findViewById(R.id.textView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            User_Name = extras.getString("User_Name");
            Password = extras.getString("Password");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                currImageURI = data.getData();

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {


                            insert_image_asy tast = new insert_image_asy();
                            tast.execute();

                        }
                        catch (Exception ex)
                        {
                            txvResult.setText(ex.toString());
                        }
                    }
                });

                thread.start();

            }
            txvResult.setText(getRealPathFromURI(currImageURI).toString());
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String result;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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
            Insert_Image_ client = new Insert_Image_();
            response = client.Do_The_work(User_Name,Password,getRealPathFromURI(currImageURI),"no");

            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            txvResult.setText(result);
        }
    }
}
