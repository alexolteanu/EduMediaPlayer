package com.edu.edumediaplayer;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.edu.edumediaplayer.fileselection.AssetImporter;
import com.edu.edumediaplayer.fileselection.FileSelectionScreen;
import com.edu.edumediaplayer.playback.PlaybackScreen;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("mp3decoder");
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FileSelectionScreen fileSelectionScreen;
    private PlaybackScreen playbackScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileSelectionScreen = new FileSelectionScreen();
        playbackScreen = new PlaybackScreen();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public void playSong(String path) {
        mViewPager.setCurrentItem(1);
        playbackScreen.playSong(path);
    }

    public void refreshFileList() {
        fileSelectionScreen.refreshFileList();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position==0)
                return fileSelectionScreen;
            return playbackScreen;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            onPermissionGranted();
        } else {
            Toast.makeText(getApplicationContext(), "Permission not granted. You will not be able to use the application", Toast.LENGTH_LONG).show();
        }
    }

    public void onPermissionGranted() {
        AssetImporter asstImp = new AssetImporter(this);
        asstImp.copyAssets("music");
        refreshFileList();
    }
}
