package com.edu.edumediaplayer.playback;


public class MP3Reader {
    public native static SongFileInfo readHeader(String fis);

    public native static short[] readPcm(String fis);
}
