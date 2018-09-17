package com.example.isaia.sss_mobile_app;

        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
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
        import android.widget.Toast;
        import  com.example.isaia.sss_mobile_app.Audio_Recorder.*;
        import com.example.isaia.sss_mobile_app.Database.DBHelper;
        import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Count_VNs;


public class Main_Activity_Voice_Notes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , RecordingSampler.CalculateVolumeListener{

    private View recordButton;
    private RecordingSampler mRecordingSampler;
    private VisualizerView mVisualizerView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main___voice__notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try
        {
            mVisualizerView3 = (VisualizerView) findViewById(R.id.visualizer3);
            mRecordingSampler = new RecordingSampler(getApplicationContext());
            mRecordingSampler.setVolumeListener(this);
            mRecordingSampler.setSamplingInterval(100);
            mRecordingSampler.link(mVisualizerView3);
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recordButton = (View) findViewById(R.id.start);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRecordingSampler.startRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mRecordingSampler.stopRecording();
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            //Do nothing
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main__activity__voice__notes, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setTitle("Logout");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            try
            {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                intent.putExtra("From_Logout","true");
                startActivity(intent);
            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
            }

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
            count_VNS_FOR_SETTINGS_asy task = new count_VNS_FOR_SETTINGS_asy();
            task.execute();
        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        mRecordingSampler.release();
        super.onDestroy();
    }
    @Override
    public void onCalculateVolume(int volume) {
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
                if(Integer.parseInt(result) > 10 || Integer.parseInt(result) == 10)
                {
                    Intent intent = new Intent(getApplicationContext(),Settings_With_Drawer.class);
                    startActivity(intent);

                }
                else
                {
                    //Do nothing
                }
            }
        }
    }
}

