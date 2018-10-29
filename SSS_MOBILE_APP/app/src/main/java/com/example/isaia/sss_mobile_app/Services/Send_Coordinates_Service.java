package com.example.isaia.sss_mobile_app.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_Device_Coordinate;


public class Send_Coordinates_Service extends Service {
    private LocationManager locationManager;
    private Double lat;
    private Double longi;
    private LocationListener locationListener;
    private Context context;
    private boolean loop;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            context = getApplicationContext();
            lat = 0.0;
            longi = 0.0;
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lat = location.getLatitude();
                    longi = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    super.run();
                    int capture = 1;
                    loop = true;
                    while (capture == 1 && loop == true) {

                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location != null)
                        {
                            lat = location.getLatitude();
                            longi = location.getLongitude();
                        }
                        insert_coordinates coord = new  insert_coordinates();
                        coord.execute();
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();

        }
        catch (Exception ex)
        {
        }

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        loop = false;
    }
    private class insert_coordinates extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            try
            {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wInfo = wifiManager.getConnectionInfo();
                String macAddress = wInfo.getMacAddress();
                Insert_Device_Coordinate insert = new Insert_Device_Coordinate();
                response = insert.Do_The_work(macAddress,String.valueOf(longi),String.valueOf(lat));
            }
            catch (Exception ex)
            {
                response = ex.toString();
            }


            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}
