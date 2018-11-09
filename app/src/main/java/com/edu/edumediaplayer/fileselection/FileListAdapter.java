package com.edu.edumediaplayer.fileselection;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.edumediaplayer.FavoritesManager;
import com.edu.edumediaplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class FileListAdapter extends ArrayAdapter<FileListItem> {

    static String ROOT = "/";

    private final Context context;
    private List<FileListItem> values;

    private final FileToListTransformer transformer;
    private final FileSystemReader reader;
    private FileListItem selection;
    private String crtPath;
    private final FavoritesManager favmgr;


    public FileListAdapter(Activity activity) {
        super(activity, -1);
        context = activity;

        transformer = new FileToListTransformer();
        reader = new FileSystemReader(activity);
        favmgr = new FavoritesManager(activity);

        startWithHomeFolder();
    }

    public void startWithHomeFolder() {
        updateFileList(ExternalStorage.getHomeFolder());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        final FileListItem crtItem = values.get(position);

        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(values.get(position).getLabel());

        rowView.setBackgroundColor(context.getResources().getColor(values.get(position).getColor()));

        final ImageView star = rowView.findViewById(R.id.star);
        star.setVisibility(favmgr.isStarShown(values.get(position))?View.VISIBLE:View.INVISIBLE);
        star.setImageResource(favmgr.isFavorite(values.get(position).getPath())?android.R.drawable.star_big_on:android.R.drawable.star_big_off);

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songPath = values.get(position).getPath();
                if (favmgr.isFavorite(songPath)) {
                    favmgr.removeFavorite(songPath);
                    star.setImageResource(android.R.drawable.star_big_off);
                } else {
                    favmgr.setFavorite(songPath);
                    star.setImageResource(android.R.drawable.star_big_on);
                }
            }
        });
        return rowView;
    }

    /**
     *
     * @param item  item that was clicked
     * @return      true if item was directory, false if it was a file
     */
    public boolean itemClicked(FileListItem item) {
        File itemFile = new File(item.getPath());
        if (itemFile.isDirectory()) {
            changePath(itemFile.getPath());
            return true;
        } else {
            selection = item;
            this.notifyDataSetChanged();
        }
        return false;
    }

    private void changePath(String newPath) {
        updateFileList(newPath);
    }

    private void updateFileList(String newPath) {
        try {
            reader.cacheMp3sOnDevice();
        } catch(SecurityException se) {
            Log.d("Emp", se.getMessage());
        }
        crtPath = newPath;
        values = transformer.transform(reader.getFiles(crtPath));
        if (!newPath.equals(ROOT)) {
            values.add(0, new FileListItem("..",new File(crtPath).getParent()));
        }
        update();
        selection = null;
    }

    void update() {
        this.clear();
        this.addAll(values);
        this.notifyDataSetChanged();
    }

    public FileListItem getSelection() {
        return selection;
    }

    public void refreshFileList() {
        updateFileList(crtPath);
    }


    private class FileToListTransformer{
        private List<FileListItem> transform(File[] files) {
            List<FileListItem> items = new ArrayList<>();
            if (files!=null) {
                for (File file : files) {
                    items.add(new FileListItem(file.getName(), file.getAbsolutePath()));
                }
            }
            Collections.sort(items);
            return items;
        }
    }

}

