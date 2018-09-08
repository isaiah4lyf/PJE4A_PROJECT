package com.example.isaia.sss_mobile_app.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Services.Predict_User_Service;
import com.example.isaia.sss_mobile_app.Services.Take_Pictures_Service;


public class BootCompletedIntentReceiver extends BroadcastReceiver implements View.OnKeyListener {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, Predict_User_Service.class);
            context.startService(pushIntent);
            Intent pushIntent_2 = new Intent(context, Take_Pictures_Service.class);
            context.startService(pushIntent_2);
        }

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_HOME){
            //Send your own custom broadcast message

        }
        return false;
    }
}