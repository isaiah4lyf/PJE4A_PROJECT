package com.example.isaia.sss_mobile_app.Services;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class Ring_Device_Service extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String Password;
    private Ringtone ringtone;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try
        {
            Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext().getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(),  defaultRingtoneUri);
            //ringtone.setStreamType(AudioManager.STREAM_RING);   // Only plays out loud when the phone is not silient
            ringtone.setStreamType(AudioManager.STREAM_ALARM);    // Only plays out loud even when the phone is silient
            ringtone.play();
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ringtone.stop();
    }

}
