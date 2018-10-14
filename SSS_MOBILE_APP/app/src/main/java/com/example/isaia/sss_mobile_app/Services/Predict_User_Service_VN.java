package com.example.isaia.sss_mobile_app.Services;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Audio_Recorder.RecordingSampler;
import com.example.isaia.sss_mobile_app.Audio_Recorder.RecordingSampler_For_Prediction;
import com.example.isaia.sss_mobile_app.Audio_Recorder.VisualizerView;
import com.example.isaia.sss_mobile_app.R;

public class Predict_User_Service_VN extends Service implements RecordingSampler_For_Prediction.CalculateVolumeListener{

    private RecordingSampler_For_Prediction mRecordingSampler;
    private VisualizerView mVisualizerView3;
    private  Dialog dialog;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dialog = new Dialog(getApplicationContext(),R.style.pred_vn_dialog);
        dialog.setContentView(R.layout.voice_note_prediction_pop_up);
        dialog.setTitle("S.S.S: Voice Recognition");
        final ImageButton recordButton = (ImageButton) dialog.findViewById(R.id.start);
        try
        {
            mVisualizerView3 = (VisualizerView) dialog.findViewById(R.id.visualizer3);
            mRecordingSampler = new RecordingSampler_For_Prediction(getApplicationContext());
            mRecordingSampler.setVolumeListener(this);
            mRecordingSampler.setSamplingInterval(100);
            mRecordingSampler.link(mVisualizerView3);
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRecordingSampler.startRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mRecordingSampler.stopRecording();
                    recordButton.setEnabled(false);
                }
                return false;
            }
        });
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //Do nothing
                }
                return true;
            }
        });
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
    @Override
    public void onCalculateVolume(int volume) {
    }
}
