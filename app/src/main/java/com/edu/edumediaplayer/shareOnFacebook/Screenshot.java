package com.edu.edumediaplayer.shareOnFacebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.edu.edumediaplayer.BuildConfig;

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

    public static String storeScreenshot(Bitmap screenshot, String filename) {
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
                    return imageFile.toString();
                }

            } catch (Exception e) {
                Log.i("Exception:", "No output file to close.");
            }

        }
        return null;
    }

    public static Intent shareScreenshot(String sharePath, View view) {
        File file = new File(sharePath);
        Uri uri = FileProvider.getUriForFile(view.getContext(),
                BuildConfig.APPLICATION_ID + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }
}
