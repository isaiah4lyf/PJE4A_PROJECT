package com.example.isaia.sss_mobile_app;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Services.Predict_User_Image_Preview;
import com.ramotion.foldingcell.FoldingCell;

public class Main_Menu extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

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

            RelativeLayout previewLayout = (RelativeLayout) findViewById(R.id.preview);
            previewLayout.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent serviceIntent = new Intent(getApplicationContext(),Predict_User_Image_Preview.class);
                            startService(serviceIntent);
                        }
                    }

            );
            RelativeLayout previewLayout2 = (RelativeLayout) findViewById(R.id.uploadVnsLayout);
            previewLayout2.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent serviceIntent = new Intent(getApplicationContext(),Main_Activity_Voice_Notes.class);
                            startActivity(serviceIntent);
                        }
                    }

            );
            RelativeLayout previewLayout3 = (RelativeLayout) findViewById(R.id.uploadImagesLayout);
            previewLayout3.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent serviceIntent = new Intent(getApplicationContext(),Main_Activity_Images.class);
                            startActivity(serviceIntent);
                        }
                    }

            );
            RelativeLayout previewLayout4 = (RelativeLayout) findViewById(R.id.settingsLayout);
            previewLayout4.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent serviceIntent = new Intent(getApplicationContext(),Settings_With_Drawer.class);
                            startActivity(serviceIntent);
                        }
                    }
            );


        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }


    }

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
        getMenuInflater().inflate(R.menu.settings__with__drawer, menu);
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


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
