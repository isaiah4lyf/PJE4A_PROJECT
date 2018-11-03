package com.example.isaia.sss_mobile_app.Lists_Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.isaia.sss_mobile_app.R;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{

    private List<Advert_Data> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;


    public MyRecyclerViewAdapter(Context context, List<Advert_Data> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());
        holder.description.setText(mData.get(position).getDescription());
        if( mData.get(position).getImageUrl().equals("false"))
        {
            holder.videoCard.setVisibility(View.VISIBLE);
            Uri video = Uri.parse( mData.get(position).getVideoUrl());
            holder.videoView.setVideoURI(video);
            holder.videoView.seekTo(1);
        }
        else
        {
            holder.imageCard.setVisibility(View.VISIBLE);
            new DownloadImageTask(holder.image)
                    .execute(mData.get(position).getImageUrl());
        }

    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        CardView videoCard;
        CardView imageCard;
        TextView description;
        ImageView image;
        ImageButton playButton;
        VideoView videoView;
        boolean playStatus;
        boolean threadStatus;
        SeekBar seek;

        ViewHolder(View itemView) {
            super(itemView);
            videoCard = itemView.findViewById(R.id.videoCard);
            imageCard = itemView.findViewById(R.id.imagecardview);
            videoView = itemView.findViewById(R.id.videoView);
            description = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.image);
            playButton = itemView.findViewById(R.id.playButton);
            title = itemView.findViewById(R.id.title);
            seek = itemView.findViewById(R.id.seekBar2);
            seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                    int progress  = seekBar.getProgress();
                    videoView.seekTo(progress*1000);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    // TODO Auto-generated method stub

                }
            });
            playStatus = false;
            videoCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playButton.setVisibility(View.VISIBLE);
                    playButton.invalidate();
                    if(playStatus == true)
                    {
                        Thread thread2 = new Thread() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                super.run();
                                try {
                                    Thread.sleep(5000);
                                    playButton.setVisibility(View.INVISIBLE);
                                    playButton.invalidate();
                                }
                                catch (Exception ex){}

                            }
                        };
                        thread2.start();
                    }
                }
            });
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    long duration = videoView.getDuration();
                    seek.setMax((int)duration/1000);
                    playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(playStatus == false)
                            {
                                videoView.start();
                                playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                                threadStatus = true;
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        super.run();
                                        try {
                                            while (threadStatus == true) {
                                                seek.setProgress(videoView.getCurrentPosition()/1000);
                                            }
                                            Thread.sleep(1000);
                                        }
                                        catch (Exception ex){}

                                    }
                                };
                                thread.start();
                                Thread thread2 = new Thread() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        super.run();
                                        try {
                                            Thread.sleep(5000);
                                            playButton.setVisibility(View.INVISIBLE);
                                            playButton.invalidate();
                                        }
                                        catch (Exception ex){}

                                    }
                                };
                                thread2.start();
                                playStatus = true;

                            }
                            else
                            {
                                threadStatus = false;
                                playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                videoView.pause();
                                playStatus = false;
                            }
                        }
                    });



                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id).getTitle();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}