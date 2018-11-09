package com.edu.edumediaplayer.fileselection;


import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.edu.edumediaplayer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetImporter {

    private Activity activity;

    public AssetImporter(Activity activity) {
        this.activity = activity;
    }

    public void copyAssets(String path) {
        int successfullyCopied = 0;
        File destination = new File(ExternalStorage.getHomeFolder()+"/Download");
        if (!destination.exists()) {
            destination = new File(ExternalStorage.getHomeFolder());
        }
        AssetManager assetManager = activity.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(path);
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(path+"/"+filename);
                File outFile = new File(destination.getPath(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                successfullyCopied++;
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(outFile)));
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
                Toast.makeText(activity.getApplicationContext(), R.string.assetCopyFailed, Toast.LENGTH_SHORT).show();
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.assetCopySuccessful, successfullyCopied), Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
