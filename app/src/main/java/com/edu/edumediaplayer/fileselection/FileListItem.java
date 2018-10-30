package com.edu.edumediaplayer.fileselection;

import android.support.annotation.NonNull;

import java.io.File;


public class FileListItem implements Comparable<FileListItem> {
    private String label;
    private String path;

    public FileListItem(String label, String path) {
        this.label = label;
        this.path = path;
    }

    public String getLabel() {
        return label;
    }

    public String getPath() {
        return path;
    }

    public int getColor() { return ColorCoding.getColor(new File(path).isDirectory());}

    @Override
    public int compareTo(@NonNull FileListItem o) {
        int isDir = new File(path).isDirectory() ? 0 : 1;
        int oIsDir = new File(o.getPath()).isDirectory() ? 0 : 1;
        if (isDir==oIsDir)
            return label.compareTo(o.label);
        return isDir-oIsDir;
    }

    public boolean isDirectory() {
        return new File(path).isDirectory();
    }
}