package com.houranos.beatdown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rei on 3/12/18.
 */

public class SongListAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater = null;
    private ArrayList<Song> songList;

    public SongListAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent,false);

        ((TextView)convertView.findViewById(android.R.id.text1)).setText(songList.get(position).getName());

        return convertView;
    }
}
