package com.example.isaia.sss_mobile_app;


import android.content.ContentResolver;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Intent;
import android.speech.RecognizerIntent;
import java.util.ArrayList;
import java.util.Locale;



public class MainActivity_Voice_Notes  extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__voice__notes);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT","audio/AMR");
        intent.putExtra("android.speech.extra.GET_AUDIO",true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(getApplicationContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(getApplicationContext(), result.get(0), Toast.LENGTH_SHORT).show();
                    Uri audiUri = data.getData();
                    ContentResolver contentResolver = getContentResolver();
                    try {
                        InputStream filestream = contentResolver.openInputStream(audiUri);
                        byte[] buffer = new byte[filestream.available()];
                        filestream.read(buffer);
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "MyCameraApp");
                        String mFileName2 = mediaStorageDir.getAbsolutePath();
                        mFileName2 += "/AUD_"+ timeStamp + ".mp4";
                        File audioFile = new File(mFileName2);
                        FileOutputStream fos = new FileOutputStream(audioFile);
                        fos.write(buffer);
                        fos.close();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
