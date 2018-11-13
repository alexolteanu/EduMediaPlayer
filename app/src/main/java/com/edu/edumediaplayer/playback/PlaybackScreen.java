package com.edu.edumediaplayer.playback;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.edumediaplayer.FavoritesManager;
import com.edu.edumediaplayer.MainActivity;
import com.edu.edumediaplayer.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlaybackScreen extends Fragment {

    MediaPlayer mediaPlayer;
    Visualizer vis;
    boolean playing;
    private FavoritesManager favmgr;
    private ImageView star;
    private Handler myHandler;
    private final int TIMESPAN = 15000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.screen_playback, container, false);

        final ImageButton playbtn = rootView.findViewById(R.id.play_pause_btn);
        playbtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                if (playing) {
                    playbtn.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                    if (mediaPlayer!=null) mediaPlayer.pause();
                    playing = false;
                } else {
                    playbtn.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                    if (mediaPlayer!=null) {
                        mediaPlayer.start();
                        startTimer();
                    }
                    playing = true;
                }
            }
        });

        final ImageButton fwdbtn = rootView.findViewById(R.id.fwd_btn);
        fwdbtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                int crtTime = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                if (duration - crtTime > TIMESPAN)
                    mediaPlayer.seekTo(crtTime+TIMESPAN);
            }
        });

        final ImageButton bwdbtn = rootView.findViewById(R.id.bwd_btn);
        bwdbtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                int crtTime = mediaPlayer.getCurrentPosition();
                if (crtTime > TIMESPAN)
                    mediaPlayer.seekTo(crtTime-TIMESPAN);
            }
        });

        favmgr = new FavoritesManager(getActivity());
        star = rootView.findViewById(R.id.star);

        BarChart topBarChart = (BarChart) rootView.findViewById(R.id.topChart);
        BarChart bottomBarChart = (BarChart) rootView.findViewById(R.id.bottomChart);
        ImageView progressBar = (ImageView) rootView.findViewById(R.id.progressBar);
        vis = new Visualizer(topBarChart, bottomBarChart, progressBar, getActivity());
        List<Integer> entries = new ArrayList <>();
        for (int i=0; i<240; i++) {
            entries.add((int)Math.round(Math.random()*500));
        }
        vis.setData(entries,entries);

        return rootView;
    }

    public void playSong(final String path) {
        SongDetailsExtractor extractor = new SongDetailsExtractor(path);
        ((TextView)(getActivity().findViewById(R.id.artist))).setText(extractor.getArtist());
        ((TextView)(getActivity().findViewById(R.id.title))).setText(extractor.getTitle());
        star.setImageResource(favmgr.isFavorite(path)?android.R.drawable.star_big_on:android.R.drawable.star_big_off);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favmgr.isFavorite(path)) {
                    favmgr.removeFavorite(path);
                    star.setImageResource(android.R.drawable.star_big_off);
                } else {
                    favmgr.setFavorite(path);
                    star.setImageResource(android.R.drawable.star_big_on);
                }
                ((MainActivity)getActivity()).refreshFileList();
            }
        });

        playing = true;

        if (mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            myHandler.removeCallbacksAndMessages(null);
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        startTimer();
    }

    public void startTimer() {
        final TextView time = getActivity().findViewById(R.id.time);
        myHandler = new Handler(Looper.getMainLooper());
        myHandler.postDelayed(new Runnable() {
            public void run() {
                int crtTime = mediaPlayer.getCurrentPosition();
                int finalTime = mediaPlayer.getDuration();
                time.setText(String.format("%02d:%02d / %02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) crtTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) crtTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) crtTime)),
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))
                ));
                float perc = crtTime * 1.f / finalTime;
                vis.updateProgress(perc);
                if (mediaPlayer.isPlaying())
                    myHandler.postDelayed(this, 500);
            }
        },500);
    }
}
