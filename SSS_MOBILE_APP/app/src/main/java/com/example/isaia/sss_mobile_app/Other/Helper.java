package com.example.isaia.sss_mobile_app.Other;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class Helper {
    public static boolean isAppRunning(final Context context,final String packageName)
    {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> processInfos =  activityManager.getRunningAppProcesses();
        if(processInfos != null)
        {
            for(final ActivityManager.RunningAppProcessInfo processInfo: processInfos){
                if(processInfo.processName.equals(packageName)){
                    return true;
                }
            }
        }
        return false;
    }

}
