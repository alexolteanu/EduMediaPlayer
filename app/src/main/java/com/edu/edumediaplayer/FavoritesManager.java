package com.edu.edumediaplayer;

import com.edu.edumediaplayer.fileselection.FileListItem;

public class FavoritesManager {
    public static boolean isStarShown(FileListItem fileListItem) {
        if (fileListItem.isDirectory())
            return false;
        return true;
    }
}
