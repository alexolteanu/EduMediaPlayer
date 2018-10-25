package com.edu.edumediaplayer.fileselection;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class PermissionObtainer {

    private static boolean permissionsObtained;

    public static void obtainPermissions(Activity activity) {
        if (!permissionsObtained) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requirePermissionAtRuntime(activity);
            }
            permissionsObtained = true;
        }
    }

    @TargetApi(23)
    public static void requirePermissionAtRuntime(Activity activity){
        if(ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            Toast.makeText(activity.getApplicationContext(), "Requesting permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 156);
        }
    }

}
