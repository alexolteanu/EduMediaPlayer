package com.edu.edumediaplayer.fileselection;

import android.app.Activity;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;


public class FileSystemReader {

    public FileSystemReader(Activity activity) {
        PermissionObtainer.obtainPermissions(activity);
    }

    public File[] getFiles(String path) {
        File[] files = new File(path).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".mp3");
            }
        });
        return files;
    }
}
