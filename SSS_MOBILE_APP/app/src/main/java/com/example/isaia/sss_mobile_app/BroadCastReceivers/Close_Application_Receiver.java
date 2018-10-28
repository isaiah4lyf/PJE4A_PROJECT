package com.example.isaia.sss_mobile_app.BroadCastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.example.isaia.sss_mobile_app.Login;

public class Close_Application_Receiver extends BroadcastReceiver implements View.OnKeyListener {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("Close_Application".equals(intent.getAction())) {
            Intent intent2 = new Intent(context, Login.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.putExtra("From_Logout","close_App");
            context.startActivity(intent2);
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