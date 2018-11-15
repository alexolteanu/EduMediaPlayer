package com.edu.edumediaplayer.playback;

import android.view.Surface;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class DecompressionThread extends Thread {

    private String path;
    private Visualizer vis;
    private Boolean needed;
    private Boolean songRead;
    private SongFileInfo info;

    public DecompressionThread(String path, Visualizer vis) {
        this.path = path;
        this.vis = vis;
    }

    public void run() {
        needed = true;
        songRead = false;
        vis.reset();
        try {
            setup();
            int totalRawLength = 0;
            short[] newRawData;
            do {
                newRawData = MP3Reader.readPcm(path);
                if (newRawData.length==0) {
                    songRead = true;
                }
                vis.feed(newRawData);
                totalRawLength += newRawData.length;
            } while (needed && !songRead);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
    }

    private void setup() throws FileNotFoundException, DataFormatException, Surface.OutOfResourcesException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        info = MP3Reader.readHeader(path);
        if (info.getErrorCode()<0){
            switch(info.getErrorCode()) {
                case -1:
                    throw new DataFormatException();
                case -2:
                    throw new Surface.OutOfResourcesException();
            }
        }
        vis.setBitrate(info.getRate());
    }

    public void setNeeded(Boolean val) {
        needed = val;
    }

}
