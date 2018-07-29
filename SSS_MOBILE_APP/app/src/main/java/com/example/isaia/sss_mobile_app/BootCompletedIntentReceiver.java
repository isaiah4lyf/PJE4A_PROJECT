package com.example.isaia.sss_mobile_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, Predict_User_Service.class);
            context.startService(pushIntent);
            Intent pushIntent_2 = new Intent(context, Lock_Service.class);
            context.startService(pushIntent_2);
        }

    }
}