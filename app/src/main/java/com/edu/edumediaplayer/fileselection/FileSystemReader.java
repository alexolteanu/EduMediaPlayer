package com.edu.edumediaplayer.fileselection;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class FileSystemReader {

    Activity activity;
    Set<String> mp3sOnDevice;
    FileSystemTree fsTree;

    public FileSystemReader(Activity activity) {
        this.activity = activity;
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

    public void cacheMp3sOnDevice() {
        ContentResolver contentResolver = activity.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");
        String[] selectionArgsMp3 = new String[]{ mimeType };
        Cursor songCursor = contentResolver.query(songUri, null, selectionMimeType, selectionArgsMp3, null);
        mp3sOnDevice = new HashSet<>();
        fsTree = new FileSystemTree();

        if (songCursor!=null && songCursor.moveToFirst()){
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentPath = songCursor.getString(songPath);
                mp3sOnDevice.add(currentPath);
                fsTree.addFile(currentPath);
            } while (songCursor.moveToNext());
        }
    }
}
