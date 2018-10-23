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
        import android.widget.TextView;
        import android.widget.Toast;
        import  com.example.isaia.sss_mobile_app.Audio_Recorder.*;
        import com.example.isaia.sss_mobile_app.Database.DBHelper;
        import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Count_VNs;

        import java.util.Random;


public class Main_Activity_Voice_Notes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , RecordingSampler.CalculateVolumeListener{

    private View recordButton;
    private RecordingSampler mRecordingSampler;
    private VisualizerView mVisualizerView3;
    private TextView quote_Text;
    private String[] quotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main___voice__notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        quote_Text = (TextView) findViewById(R.id.textView3);
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
        quotes = new String[16];
        quotes[0] = "'Good things happen to those who hustle - Anaïs Nin'";
        quotes[1] = "'If it matters to you, you’ll find a way - Charlie Gilkey'";
        quotes[2] = "'We are twice armed if we fight with faith - Plato'";
        quotes[3] = "'Persistence guarantees that results are inevitable - Paramahansa Yogananda'";
        quotes[4] = "'It does not matter how slowly you go as long as you do not stop - Confucius'";
        quotes[5] = "'It is better to live one day as a lion, than a thousand days as a lamb - Roman proverb'";
        quotes[6] = "'The two most important days in your life are the day you are born and they day you find out why - Mark Twain'";
        quotes[7] = "'Everything you can imagine is real – Pablo Picasso'";
        quotes[8] = "'Simplicity is the ultimate sophistication. – Leonardo da Vinci'";
        quotes[9] = "'Whatever you do, do it well – Walt Disney'";
        quotes[10] = "'There is no substitute for hard work. – Thomas Edison'";
        quotes[11] = "'The time is always right to do what is right. – Martin Luther King Jr.'";
        quotes[12] = "'May your choices reflect your hopes, not your fears. – Nelson Mandela'";
        quotes[13] = "'Normality is a paved road: it’s comfortable to walk but no flowers grow. – Vincent van Gogh'";
        quotes[14] = "'Turn your wounds into wisdom. – Oprah Winfrey'";
        quotes[15] = "'Happiness depends upon ourselves. – Aristotle'";
        Random rand = new Random();
        int i = rand.nextInt(9);			//Random
        quote_Text.setText(quotes[i]);
        recordButton = (View) findViewById(R.id.start);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRecordingSampler.startRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mRecordingSampler.stopRecording();
                    Random rand = new Random();
                    int i = rand.nextInt(9);			//Random
                    quote_Text.setText(quotes[i]);
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
        //MenuItem item = menu.findItem(R.id.action_settings);
        //item.setTitle("Logout");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            try
            {
                mRecordingSampler.release();
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
        if (id == R.id.upload_Images) {
            Intent intent = null;
            try {
                mRecordingSampler.release();
                intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Main_Activity_Images"));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.Upload_VN) {
            Intent intent = null;
            try {
                mRecordingSampler.release();
                intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Main_Activity_Voice_Notes"));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.settings) {
            count_VNS_FOR_SETTINGS_asy task = new count_VNS_FOR_SETTINGS_asy();
            task.execute();
        }  else if (id == R.id.account_man) {

        } else if (id == R.id.users) {

        }
        else if (id == R.id.add_user) {

        }
        else if (id == R.id.logoff) {
            try
            {
                mRecordingSampler.release();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                intent.putExtra("From_Logout","true");
                startActivity(intent);
            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
            }
        }
        else if (id == R.id.main_menu) {
            mRecordingSampler.release();
            Intent intent = new Intent(getApplicationContext(),Main_Menu.class);
            startActivity(intent);
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
                    mRecordingSampler.release();
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

