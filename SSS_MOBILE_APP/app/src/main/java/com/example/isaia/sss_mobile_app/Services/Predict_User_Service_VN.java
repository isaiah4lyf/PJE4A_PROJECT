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
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Audio_Recorder.RecordingSampler;
import com.example.isaia.sss_mobile_app.Audio_Recorder.RecordingSampler_For_Prediction;
import com.example.isaia.sss_mobile_app.Audio_Recorder.VisualizerView;
import com.example.isaia.sss_mobile_app.R;

import java.util.Random;

public class Predict_User_Service_VN extends Service implements RecordingSampler_For_Prediction.CalculateVolumeListener{

    private RecordingSampler_For_Prediction mRecordingSampler;
    private VisualizerView mVisualizerView3;
    private  Dialog dialog;
    private TextView quote_Text;
    private String[] quotes;

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
        quote_Text = (TextView) dialog.findViewById(R.id.textView3);
        quotes = new String[16];
        quotes[0] = "'Good things happen to those who hustle - Anaïs Nin'";
        quotes[1] = "'If it matters to you, you’ll find a way - Charlie Gilkey'";
        quotes[2] = "'We are twice armed if we fight with faith - Plato'";
        quotes[3] = "'Persistence guarantees that results are inevitable - Paramahansa Yogananda'";
        quotes[4] = "'It does not matter how slowly you go as long as you do not stop - Confucius'";
        quotes[5] = "'It is better to live one day as a lion, than a thousand days as a lamb - Roman proverb'";
        quotes[6] = "'The two most important days in your life are the day you are born and they day you find out why - Mark Twain'";
        quotes[7] = "'Everything you can imagine is real – Pablo Picasso'";
        quotes[8] = "'Simplicity is the ultimate sophistication. – Leonardo da Vinci'";
        quotes[9] = "'Whatever you do, do it well – Walt Disney'";
        quotes[10] = "'There is no substitute for hard work. – Thomas Edison'";
        quotes[11] = "'The time is always right to do what is right. – Martin Luther King Jr.'";
        quotes[12] = "'May your choices reflect your hopes, not your fears. – Nelson Mandela'";
        quotes[13] = "'Normality is a paved road: it’s comfortable to walk but no flowers grow. – Vincent van Gogh'";
        quotes[14] = "'Turn your wounds into wisdom. – Oprah Winfrey'";
        quotes[15] = "'Happiness depends upon ourselves. – Aristotle'";
        Random rand = new Random();
        int i = rand.nextInt(9);			//Random
        quote_Text.setText(quotes[i]);
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
                    Random rand = new Random();
                    int i = rand.nextInt(9);			//Random
                    quote_Text.setText(quotes[i]);
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
