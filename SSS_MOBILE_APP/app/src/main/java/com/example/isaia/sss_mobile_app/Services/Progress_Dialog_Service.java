package com.example.isaia.sss_mobile_app.Services;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.R;

public class Progress_Dialog_Service extends Service{
    private Dialog dialog;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        dialog = new Dialog(getApplicationContext(),R.style.pred_vn_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_number_pop_up);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();

    }
}
