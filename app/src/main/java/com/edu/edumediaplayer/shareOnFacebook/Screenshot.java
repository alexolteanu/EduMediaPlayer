package com.edu.edumediaplayer.shareOnFacebook;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Screenshot {

    public static Bitmap takeScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap screenshot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return screenshot;
    }

    public static void storeScreenshot(Bitmap screenshot, String filename) {
        String path = Environment.getExternalStorageDirectory().toString() + "/" + filename;
        OutputStream out = null;
        File imageFile = new File(path);

        try {
            out = new FileOutputStream(imageFile);
            screenshot.compress(Bitmap.CompressFormat.JPEG, 99, out);
            out.flush();

        } catch (FileNotFoundException e) {
            Log.i("Exception:", "File not found.");

        } catch (IOException e) {
            Log.i("Exception:", "Cannot write to output file.");

        } finally {

            try {
                if (out != null) {
                    out.close();
                }

            } catch (Exception e) {
                Log.i("Exception:", "No output file to close.");
            }

        }
    }
}
