package com.example.isaia.sss_mobile_app;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.Services.MyAdmin;
import com.example.isaia.sss_mobile_app.Services.Predict_User_Service;
import com.example.isaia.sss_mobile_app.Services.Predict_User_Service_VN;
import com.example.isaia.sss_mobile_app.Services.Take_Pictures_Service;
import com.example.isaia.sss_mobile_app.Services.Train_Images_Model_Service;
//import com.kyleduo.switchbutton.SwitchButton;

public class Settings_With_Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;
    @Override
    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__with__drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try
        {
            devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
            activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            boolean active = devicePolicyManager.isAdminActive(compName);
            compName = new ComponentName(this, MyAdmin.class);
            if (!active) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "This Application requires administrative permission to lock the device when an incorrect user is detected.");
                startActivity(intent);
            }


            final DBHelper mydb = new DBHelper(getApplicationContext());
            Switch switchButton = (Switch) findViewById(R.id.switch_button2);
            if(Integer.parseInt(mydb.Get_Image_Prediction_Service_Status()) == 1)
            {
                switchButton.setChecked(true);
                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if (!Predict_User_Service.class.equals(service.service.getClassName())) {
                        Intent serviceIntent = new Intent(getApplicationContext(),Predict_User_Service.class);
                        startService(serviceIntent);
                    }
                }

            }
            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mydb.Update_Face_Recog_Settings("1",mydb.Get_Image_Upload_Interval(),mydb.Get_Image_Upload_Service_Status(),mydb.Get_Image_Verification_Interval());
                        Intent serviceIntent = new Intent(getApplicationContext(),Predict_User_Service.class);
                        startService(serviceIntent);
                    } else if (!isChecked) {
                        mydb.Update_Face_Recog_Settings("0",mydb.Get_Image_Upload_Interval(),mydb.Get_Image_Upload_Service_Status(),mydb.Get_Image_Verification_Interval());
                        stopService(new Intent(getApplicationContext(), Predict_User_Service.class));
                    }
                }
            });

            Switch switchButton2 = (Switch) findViewById(R.id.switch_button4);
            if(Integer.parseInt(mydb.Get_Image_Upload_Service_Status()) == 1)
            {
                switchButton2.setChecked(true);
                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if (!Take_Pictures_Service.class.equals(service.service.getClassName())) {
                        Intent serviceIntent = new Intent(getApplicationContext(),Take_Pictures_Service.class);
                        startService(serviceIntent);
                    }
                }
            }
            switchButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mydb.Update_Face_Recog_Settings(mydb.Get_Image_Prediction_Service_Status(),mydb.Get_Image_Upload_Interval(),"1",mydb.Get_Image_Verification_Interval());
                        Intent serviceIntent = new Intent(getApplicationContext(),Take_Pictures_Service.class);
                        startService(serviceIntent);
                    } else if (!isChecked) {
                        mydb.Update_Face_Recog_Settings(mydb.Get_Image_Prediction_Service_Status(),mydb.Get_Image_Upload_Interval(),"0",mydb.Get_Image_Verification_Interval());
                        stopService(new Intent(getApplicationContext(), Take_Pictures_Service.class));
                    }
                }
            });
            SeekBar interval = (SeekBar)findViewById(R.id.seekBar); // make seekbar object
            interval.setProgress(Integer.parseInt(mydb.Get_Image_Upload_Interval()));
            TextView intText = (TextView)findViewById(R.id.menu3);
            intText.setText("Image Upload Interval: "+ mydb.Get_Image_Upload_Interval() +" Minutes");
            interval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    // TODO Auto-generated method stub
                    TextView intText = (TextView)findViewById(R.id.menu3);
                    intText.setText("Image Upload Interval: "+ progress +" Minutes");
                    mydb.Update_Face_Recog_Settings(mydb.Get_Image_Prediction_Service_Status(),String.valueOf(progress),mydb.Get_Image_Upload_Service_Status(),mydb.Get_Image_Verification_Interval());

                }
            });
            SeekBar pred_Interval = (SeekBar)findViewById(R.id.Verification_Interval); // make seekbar object
            pred_Interval.setProgress(Integer.parseInt(mydb.Get_Image_Verification_Interval()));
            TextView pred_Inter_Text = (TextView)findViewById(R.id.veriText);
            pred_Inter_Text.setText("Verification Interval: "+ mydb.Get_Image_Verification_Interval() +" Seconds");
            pred_Interval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    // TODO Auto-generated method stub
                    TextView intText = (TextView)findViewById(R.id.veriText);
                    intText.setText("Verification Interval: "+ progress +" Seconds");
                    mydb.Update_Face_Recog_Settings(mydb.Get_Image_Prediction_Service_Status(),String.valueOf(progress),mydb.Get_Image_Upload_Service_Status(),String.valueOf(progress));

                }
            });

            int voicePredServStatus = Integer.parseInt(mydb.Get_Voice_Prediction_Service_Status());
            int voiceUploadServiceSatus = Integer.parseInt(mydb.Get_Voice_Upload_Service_Status());
            Switch switchButton3 = (Switch) findViewById(R.id.SwitchVoicePrediction);
            if(voicePredServStatus == 1)
            {
                switchButton3.setChecked(true);
            }
            switchButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mydb.Update_Voice_Settings("1",mydb.Get_Voice_Upload_Service_Status());
                        Intent serviceIntent = new Intent(getApplicationContext(),Predict_User_Service_VN.class);
                        startService(serviceIntent);
                    } else if (!isChecked) {
                        mydb.Update_Voice_Settings("0",mydb.Get_Voice_Upload_Service_Status());
                        stopService(new Intent(getApplicationContext(), Predict_User_Service_VN.class));
                    }
                }
            });
            Switch switchButton4 = (Switch) findViewById(R.id.SwitchVoiceRecordService);
            if(voiceUploadServiceSatus == 1)
            {
                switchButton4.setChecked(true);
            }
            switchButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mydb.Update_Voice_Settings(mydb.Get_Voice_Prediction_Service_Status(),"1");
                        //starting service
                       // Intent serviceIntent = new Intent(getApplicationContext(),Predict_User_Service.class);
                        //startService(serviceIntent);
                    } else if (!isChecked) {
                        mydb.Update_Voice_Settings(mydb.Get_Voice_Prediction_Service_Status(),"0");
                        //stopping service
                       // stopService(new Intent(getApplicationContext(), Predict_User_Service.class));
                    }
                }
            });



        }
        catch (Exception e)
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
            //super.onBackPressed();
            //Do nothing
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings__with__drawer, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setTitle("Logout");
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
}
