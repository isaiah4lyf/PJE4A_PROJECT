package com.example.isaia.sss_mobile_app.BroadCastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Services.Progress_Dialog_Service;
import com.example.isaia.sss_mobile_app.Services.Register_User_Service;
import com.example.isaia.sss_mobile_app.Services.Ring_Device_Service;

public class Sms_Listener_Receiver extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from = null;
            String msgBody = null;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();

                    }
                    if(msgBody.contains("RING_DEVICE") && msg_from.equals("+15312331112"))
                    {
                        abortBroadcast();
                        Intent serviceIntent = new Intent(context, Ring_Device_Service.class);
                        context.startService(serviceIntent);
                    }
                    else if(msgBody.contains("STOP_RINGING") && msg_from.equals("+15312331112"))
                    {
                        abortBroadcast();
                        context.stopService(new Intent(context, Ring_Device_Service.class));
                    }
                    else if(msgBody.contains("Phone number confirmation sms from Smartphone Security System.") && msg_from.equals("+15312331112"))
                    {
                        abortBroadcast();
                        Intent reg_service = new Intent(context,Register_User_Service.class);
                        context.startService(reg_service);

                    }

                }catch(Exception e){
                }
            }
        }
    }
}
