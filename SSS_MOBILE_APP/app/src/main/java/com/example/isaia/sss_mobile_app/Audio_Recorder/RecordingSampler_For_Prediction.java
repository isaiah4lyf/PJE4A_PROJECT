package com.example.isaia.sss_mobile_app.Audio_Recorder;


import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_Voice_Note;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User_VN;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Sampling AudioRecord Input
 * This output send to {@link VisualizerView}
 *
 * Created by tyorikan on 2015/06/09.
 */
public class RecordingSampler_For_Prediction {

    private static final int RECORDING_SAMPLE_RATE = 44100;

    private AudioRecord mAudioRecord;
    private boolean mIsRecording;
    private int mBufSize;

    private CalculateVolumeListener mVolumeListener;
    private int mSamplingInterval = 100;
    private Timer mTimer;
    private File file;
    private File f2;
    private String User_Name;
    private String Password;
    private List<VisualizerView> mVisualizerViews = new ArrayList<>();
    private Context context;

    public RecordingSampler_For_Prediction(Context context) {
        initAudioRecord();
        this.context = context;
        DBHelper mydb = new DBHelper(context);
        User_Name = mydb.User_Name();
        Password = mydb.Password();
    }

    /**
     * link to VisualizerView
     *
     * @param visualizerView {@link VisualizerView}
     */
    public void link(VisualizerView visualizerView) {
        mVisualizerViews.add(visualizerView);
    }

    /**
     * setter of CalculateVolumeListener
     *
     * @param volumeListener CalculateVolumeListener
     */
    public void setVolumeListener(CalculateVolumeListener volumeListener) {
        mVolumeListener = volumeListener;
    }

    /**
     * setter of samplingInterval
     *
     * @param samplingInterval interval volume sampling
     */
    public void setSamplingInterval(int samplingInterval) {
        mSamplingInterval = samplingInterval;
    }

    /**
     * getter isRecording
     *
     * @return true:recording, false:not recording
     */
    public boolean isRecording() {
        return mIsRecording;
    }

    private void initAudioRecord() {
        int bufferSize = AudioRecord.getMinBufferSize(
                RECORDING_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        );

        mAudioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RECORDING_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
        );

        if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            mBufSize = bufferSize;
        }
    }

    /**
     * start AudioRecord.read
     */
    public void startRecording() {
        mTimer = new Timer();
        mAudioRecord.startRecording();
        mIsRecording = true;
        try {
            runRecording();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * stop AudioRecord.read
     */
    public void stopRecording() {
        mIsRecording = false;
        mTimer.cancel();

        if (mVisualizerViews != null && !mVisualizerViews.isEmpty()) {
            for (int i = 0; i < mVisualizerViews.size(); i++) {
                mVisualizerViews.get(i).receive(0);
            }
        }
        File f1 = file; // The location of your PCM file
        f2 = getOutputMediaFile_Wav(); // The location where you want your WAV file
        try {
            rawToWave(f1, f2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pred_user_vn tast = new pred_user_vn();
                    tast.execute();

                }
                catch (Exception ex)
                {
                }
            }
        });
        thread.start();
    }

    private void runRecording() throws FileNotFoundException {
        file = getOutputMediaFile();
        final FileOutputStream outStream = new FileOutputStream(file);
        final byte buf[] = new byte[mBufSize];
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // stop recording
                if (!mIsRecording) {
                    mAudioRecord.stop();
                    return;
                }
                mAudioRecord.read(buf, 0, mBufSize);
                try {
                    outStream.write(buf, 0, mBufSize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int decibel = calculateDecibel(buf);
                if (mVisualizerViews != null && !mVisualizerViews.isEmpty()) {
                    for (int i = 0; i < mVisualizerViews.size(); i++) {
                        mVisualizerViews.get(i).receive(decibel);
                    }
                }

                // callback for return input value
                if (mVolumeListener != null) {
                    mVolumeListener.onCalculateVolume(decibel);
                }
            }
        }, 0, mSamplingInterval);
    }

    private int calculateDecibel(byte[] buf) {
        int sum = 0;
        for (int i = 0; i < mBufSize; i++) {
            sum += Math.abs(buf[i]);
        }
        // avg 10-50
        return sum / mBufSize;
    }

    /**
     * release member object
     */
    public void release() {
        stopRecording();
        mAudioRecord.release();
        mAudioRecord = null;
        mTimer = null;
    }

    public interface CalculateVolumeListener {

        /**
         * calculate input volume
         *
         * @param volume mic-input volume
         */
        void onCalculateVolume(int volume);
    }
    private static File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Smartphone Security System");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "AUD_"+ timeStamp + ".pcm");


        return mediaFile;
    }

    private static File getOutputMediaFile_Wav(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Smartphone Security System");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "AUD_"+ timeStamp + ".wav");


        return mediaFile;
    }

    private void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, 44100); // sample rate
            writeInt(output, RECORDING_SAMPLE_RATE ); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }
    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

    private class pred_user_vn extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            try
            {
                Pred_User_VN count_class = new Pred_User_VN();
                response = count_class.Do_The_work(User_Name,Password,f2.getAbsolutePath());
            }
            catch(Exception ex)
            {
                response = ex.getMessage();
            }

            return  response;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            if(file.exists())
            {
                file.delete();
            }
            if(f2.exists())
            {
                f2.delete();
            }
        }
    }
}
