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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        List<File> files = new ArrayList<>();
        String[] filePaths = new String[0];
        if (mp3sOnDevice!=null) {
            try {
                filePaths = fsTree.getFiles(new File(path).getCanonicalFile().getPath());
            } catch (IOException e) {
                e.printStackTrace();
                filePaths = fsTree.getRootFiles();
            }
        }
        for (String filePath:filePaths) {
            files.add(new File(path+"/"+filePath));
        }
        return files.toArray(new File[files.size()]);
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
