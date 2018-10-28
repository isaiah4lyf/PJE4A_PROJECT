package com.example.isaia.sss_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.Lists_Adapters.MyRecyclerViewAdapter;
import com.example.isaia.sss_mobile_app.Lists_Adapters.Recycler_View_Adapter_Users;

import java.util.ArrayList;

public class User_And_Roles extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,  Recycler_View_Adapter_Users.ItemClickListener {
    private Recycler_View_Adapter_Users adapter;
    private Toolbar toolbar;
    private DBHelper mydb;
    private ArrayList<String> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__and__roles);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // data to populate the RecyclerView with
        mydb = new DBHelper(getApplicationContext());
        users =  mydb.Get_Users();



        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Recycler_View_Adapter_Users(this, users);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                add_User();
            }
        });
    }

    public void add_User()
    {
        LayoutInflater layoutInflater
                = (LayoutInflater)getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.add_user, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageButton btnDismiss = (ImageButton)popupView.findViewById(R.id.close);
        btnDismiss.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }});

        popupWindow.showAsDropDown(toolbar,90, 150);
        final EditText user_name= (EditText)popupView.findViewById(R.id.user_name);
        final EditText password = (EditText)popupView.findViewById(R.id.password);

        final Button send_message = (Button)popupView.findViewById(R.id.add_user);
        send_message.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(!user_name.getText().equals(""))
                {
                    if(!password.getText().toString().equals(""))
                    {
                        popupWindow.dismiss();
                        Toast.makeText(getApplicationContext(),String.valueOf(mydb.Insert_User(password.getText().toString(),user_name.getText().toString())),Toast.LENGTH_LONG).show();
                        users.add(users.size() - 1, user_name.getText().toString());
                        adapter.notifyItemInserted(users.size() - 1);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Password text box is empty!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"User name text box is empty!", Toast.LENGTH_LONG).show();
                }

            }});

        final TextView reg_user = (TextView)popupView.findViewById(R.id.Reg_User);
        reg_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
                Intent intent = new Intent(getApplicationContext(),Reg_User.class);
                startActivity(intent);
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.update();
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
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.user__and__roles, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}