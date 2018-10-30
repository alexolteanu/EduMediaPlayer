package com.edu.edumediaplayer;

import android.app.Activity;
import android.content.SharedPreferences;

import com.edu.edumediaplayer.fileselection.FileListItem;

public class FavoritesManager {

    public static final String PREFS_NAME = "Favorites";
    private SharedPreferences favorites;
    private SharedPreferences.Editor editor;

    public FavoritesManager(Activity activity) {
        favorites = activity.getSharedPreferences(PREFS_NAME, 0);
        editor = favorites.edit();
    }

    public boolean isStarShown(FileListItem fileListItem) {
        if (fileListItem.isDirectory())
            return false;
        return true;
    }

    public boolean isFavorite(String path) {
        return favorites.getBoolean(path, false);
    }

    public void setFavorite(String path) {
        editor.putBoolean(path, true);
        editor.apply();
    }

    public void removeFavorite(String path) {
        editor.remove(path);
        editor.apply();
    }
}
