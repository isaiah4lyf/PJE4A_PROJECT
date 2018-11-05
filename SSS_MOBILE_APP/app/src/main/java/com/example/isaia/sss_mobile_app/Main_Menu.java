package com.example.isaia.sss_mobile_app;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.isaia.sss_mobile_app.Lists_Adapters.Advert_Data;
import com.example.isaia.sss_mobile_app.Lists_Adapters.MyRecyclerViewAdapter;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Load_Last_News_Feed;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Load_More_News_Feed;
import com.example.isaia.sss_mobile_app.Services.Predict_User_Image_Preview;
import com.example.isaia.sss_mobile_app.Services.SSS_Vision_Service;
import com.ramotion.foldingcell.FoldingCell;

import java.io.File;
import java.util.ArrayList;

public class Main_Menu extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,  MyRecyclerViewAdapter.ItemClickListener {
    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView sss_vision;
    private boolean loading = true;
    private ArrayList<Advert_Data> dataArray;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);
        try
        {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            dataArray = new ArrayList<>();
            load_Last_asy task = new load_Last_asy();
            task.execute();

            recyclerView = findViewById(R.id.rvAnimals);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MyRecyclerViewAdapter(this, dataArray);
            adapter.setClickListener(this);


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }


                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {

                    if (loading) {
                        if (dy > 0) //check for scroll down
                        {
                            int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                            int  totalItemCount = recyclerView.getLayoutManager().getItemCount();
                            int pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                loading = false;
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        super.run();
                                        load_more_asy task =  new load_more_asy();
                                        task.execute();
                                    }
                                };
                                thread.start();
                            }

                        }
                    }
                }
            });
            recyclerView.setAdapter(adapter);
            sss_vision = findViewById(R.id.sss_vision);
            sss_vision.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SSS_Vision_Service.class);
                    startService(intent);
                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }


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
    public void onItemClick(View view, int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.settings__with__drawer, menu);
        //MenuItem item = menu.findItem(R.id.action_settings);
        //item.setTitle("Logout");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(),Login.class);
            intent.putExtra("From_Logout","true");
            startActivity(intent);
            return true;
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


        }  else if (id == R.id.account_man) {

        } else if (id == R.id.users) {
            Intent intent = new Intent(getApplicationContext(),User_And_Roles.class);
            startActivity(intent);
        }
        else if (id == R.id.add_user) {

        }
        else if (id == R.id.logoff) {
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
        else if (id == R.id.main_menu) {
             Intent intent = new Intent(getApplicationContext(),Main_Menu.class);
             startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class load_Last_asy extends AsyncTask<String, Void, Advert_Data> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected Advert_Data doInBackground(String... urls) {

            Advert_Data response = null;
            try
            {
                if(haveNetworkConnection() == true)
                {
                    Load_Last_News_Feed loadLast = new Load_Last_News_Feed();
                    response = loadLast.Do_The_work();
                }
                else
                {
                    response.setId("No Internet Connection!");
                }

            }
            catch(Exception ex)
            {
                response.setId("Error: " + ex.getLocalizedMessage());
            }
            return  response;
        }
        @Override
        protected void onPostExecute(Advert_Data result) {
            if (!result.getId().equals("false")) {
                if (!result.getId().equals("No Internet Connection!") || !result.getId().contains("Error:")) {
                    dataArray.add(result);
                    adapter.notifyItemInserted(dataArray.size() - 1);
                    loading = true;
                    load_more_asy task = new load_more_asy();
                    task.execute();
                } else {
                    Toast.makeText(getApplicationContext(), result.getId(), Toast.LENGTH_LONG).show();
                }
            } else {
                loading = true;
            }
        }
    }
    private class load_more_asy extends AsyncTask<String, Void, Advert_Data> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected Advert_Data doInBackground(String... urls) {

            Advert_Data response = null;
            try
            {

                if(haveNetworkConnection() == true)
                {
                    Load_More_News_Feed feed = new Load_More_News_Feed();
                    response = feed.Do_The_work(dataArray.get(dataArray.size() -1 ).getId());
                }
                else {
                    response.setId("No Internet Connection!");
                }

            }
            catch(Exception ex) {
                response.setId("Error: " + ex.getLocalizedMessage());
            }
            return  response;
        }
        @Override
        protected void onPostExecute(Advert_Data result) {
            //if you started progress dialog dismiss it here
            if (!result.getId().equals("false")) {
                if (!result.getId().equals("No Internet Connection!") || !result.getId().contains("Error:")) {
                    dataArray.add(result);
                    adapter.notifyItemInserted(dataArray.size() - 1);
                    loading = true;
                } else {
                    Toast.makeText(getApplicationContext(), result.getId(), Toast.LENGTH_LONG).show();
                }

            } else {
                loading = false;
            }
        }
    }
}
