package com.edu.edumediaplayer.fileselection;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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


    public FileListAdapter(Activity activity) {
        super(activity, -1);
        context = activity;

        transformer = new FileToListTransformer();
        reader = new FileSystemReader(activity);

        startWithHomeFolder();
    }

    public void startWithHomeFolder() {
        updateFileList(ExternalStorage.getHomeFolder());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        final FileListItem crtItem = values.get(position);

        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(values.get(position).getLabel());

        rowView.setBackgroundColor(context.getResources().getColor(values.get(position).getColor()));

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

