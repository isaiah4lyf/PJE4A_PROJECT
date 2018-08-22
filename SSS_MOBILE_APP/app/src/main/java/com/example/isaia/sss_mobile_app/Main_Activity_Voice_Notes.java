package com.example.isaia.sss_mobile_app;

        import android.Manifest;
        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.media.MediaPlayer;
        import android.media.MediaRecorder;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Environment;
        import android.support.annotation.NonNull;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.ViewGroup;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.example.isaia.sss_mobile_app.Database.DBHelper;
        import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_Voice_Note;

        import java.io.File;
        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.Date;

public class Main_Activity_Voice_Notes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener /*, View.OnTouchListener */{

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private String User_Name;
    private String Password;
    float dX;
    float dY;
    int lastAction;
    private boolean permissionToRecordAccepted = false;
    private View binView;
    private View dragView;
    private View recordButton;
    private  ImageButton recordButton2;
    private boolean fileDelete;
    private TextView timeText;
    private float InitialRecordY;
    private ImageButton bin_shake;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main___voice__notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DBHelper mydb = new DBHelper(getApplicationContext());
        User_Name = mydb.User_Name();
        Password = mydb.Password();

        timeText = (TextView)findViewById(R.id.textView3);
        fileDelete = false;
        recordButton = (View) findViewById(R.id.start);
        /*
        recordButton2 = (ImageButton)findViewById(R.id.start);
        recordButton2.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera

                        try
                        {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int count = 1;
                                    while (true)
                                    {
                                        timeText.setText(String.valueOf(count));
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            thread.start();
                        }
                        catch(Exception e)
                        {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );
        */
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // start your timer


                    onRecord(true);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // stop your timer.
                    onRecord(false);
                    //thread.interrupt();
                }
                return false;
            }
        });

        binView = findViewById(R.id.bin);
        binView.setVisibility(View.GONE);
        /*
        binView = findViewById(R.id.bin);
        binView.setVisibility(View.GONE);
        dragView = findViewById(R.id.start);
        dragView.setOnTouchListener(this);
        fileDelete = false;
        @SuppressLint("ResourceType")
        final Animation animShake = AnimationUtils.loadAnimation(this, R.xml.shake_button);
        bin_shake = (ImageButton) findViewById(R.id.bin);
        bin_shake.startAnimation(animShake);
        */
    }
    /*
    @Override
    public boolean onTouch(View view, MotionEvent event) {


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //Start Recording here

                onRecord(true);
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                InitialRecordY = dragView.getY();
                lastAction = MotionEvent.ACTION_DOWN;
                binView.setVisibility(View.VISIBLE);
                break;

            case MotionEvent.ACTION_MOVE:
                if(dragView.getY() > binView.getY())
                {
                    view.setY(event.getRawY() + dY);
                }
                //view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                //Stop Recording here
                onRecord(false);
                if(dragView.getY() <= binView.getY())
                {
                    fileDelete = true;
                }

                view.setY(InitialRecordY);
                binView.setVisibility(View.GONE);
                lastAction = MotionEvent.ACTION_UP;
                break;

            default:
                return false;
        }

        return true;
    }
    */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main__activity__voice__notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.account_man) {
            // Handle the camera action
        } else if (id == R.id.upload_Images) {
            Intent intent = null;
            try {
                intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Main_Activity_Images"));
                startActivity(intent);

            } catch (ClassNotFoundException e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.Upload_VN) {
            Intent intent = null;
            try {
                intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Main_Activity_Voice_Notes"));
                startActivity(intent);

            } catch (ClassNotFoundException e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.settings) {
            Intent intent = null;
            try {
                intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Settings_With_Drawer"));
                startActivity(intent);

            } catch (ClassNotFoundException e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }


        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Audio recording
    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        // Byte array for audio record
        File output_audio = getOutputMediaFile();
        mFileName = output_audio.getAbsolutePath();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncodingBitRate(16);
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
        if(fileDelete == false)
        {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Insert_VN tast = new Insert_VN();
                        tast.execute();

                    }
                    catch (Exception ex)
                    {
                    }
                }
            });
            thread.start();
        }
        else
        {
            fileDelete = false;
            File audioFile = new File(mFileName);
            if(audioFile.exists())
            {
                audioFile.delete();
            }
            Toast.makeText(getApplicationContext(),"Recording cancelled!",Toast.LENGTH_LONG).show();
        }
    }



    private static File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Smartphone Security System");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "AUD_"+ timeStamp + ".wav");


        return mediaFile;
    }

    private class Insert_VN extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            Insert_Voice_Note count_class = new Insert_Voice_Note();
            response = count_class.Do_The_work(User_Name,Password,mFileName);
            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            File audioFile = new File(mFileName);
            if(audioFile.exists())
            {
                audioFile.delete();
            }
        }
    }
}

