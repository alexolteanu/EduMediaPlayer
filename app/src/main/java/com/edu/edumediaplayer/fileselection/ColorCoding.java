package com.edu.edumediaplayer.fileselection;

import com.edu.edumediaplayer.R;

class ColorCoding {

    public static int getColor(boolean directory) {
        if (directory) return android.R.color.white;
        return R.color.folderBg;
    }
}
