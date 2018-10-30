package com.edu.edumediaplayer.playback;

import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
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

import java.io.IOException;

public class PlaybackScreen extends Fragment {

    MediaPlayer mediaPlayer;
    boolean playing;
    private FavoritesManager favmgr;
    private ImageView star;


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
                    if (mediaPlayer!=null) mediaPlayer.start();
                    playing = true;
                }
            }
        });

        favmgr = new FavoritesManager(getActivity());
        star = rootView.findViewById(R.id.star);

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
    }
}
