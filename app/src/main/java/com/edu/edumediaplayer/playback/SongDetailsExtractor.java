package com.edu.edumediaplayer.playback;

import android.media.MediaMetadataRetriever;

import java.io.File;

class SongDetailsExtractor {

    String artist;
    String title;

    public SongDetailsExtractor(String path) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        File f = new File(path);
        String filename = f.getName().replaceFirst("[.][^.]+$", "");
        String[] parts = filename.split("[-_]");

        if (artist==null) {
            if (parts.length>1 && parts[0].length()>0) {
                artist = parts[0].trim();
            } else {
                artist = "";
            }
        }

        if (title==null) {
            title = parts[parts.length-1].trim();
        }
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }
}
