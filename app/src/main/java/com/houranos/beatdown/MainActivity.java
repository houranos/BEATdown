package com.houranos.beatdown;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final ArrayList<Song> songList = new ArrayList<>();
    private ListView songListView;
    static final int OPEN_DOCUMENT_REQUEST = 1234;
    SharedPreferences data;
    SharedPreferences.Editor editor;
    private SongListAdapter songListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        data = getSharedPreferences("songList", Context.MODE_PRIVATE);
        editor = data.edit();
        songListView = (ListView) findViewById(R.id.song_list);
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, PlayMediaActivity.class);
                intent.putExtra("FILEPATH", songList.get(i).getPath());
                startActivity(intent);
            }
        });
        songListAdapter = new SongListAdapter(this);
        songListAdapter.setSongList(songList);
        Set<String> songSet = data.getStringSet("songs", new HashSet<String>());
        for (String songPath : songSet) {
            songList.add(new Song(Uri.parse(songPath).getLastPathSegment(), songPath));
        }
        songListView.setAdapter(songListAdapter);
        registerForContextMenu(songListView);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("video/*");
                startActivityForResult(intent, OPEN_DOCUMENT_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_DOCUMENT_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Set<String> set = new HashSet<>();
                set.addAll(MainActivity.this.data.getStringSet("songs", new HashSet<String>()));
                set.add(data.getDataString());
                editor.putStringSet("songs", set);
                editor.apply();
                songList.add(new Song(Uri.parse(data.getDataString()).getLastPathSegment(), data.getDataString()));
                songListAdapter.setSongList(songList);
                songListView.setAdapter(songListAdapter);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_song, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                Set<String> set = new HashSet<>();
                set.addAll(data.getStringSet("songs", new HashSet<String>()));
                set.remove(songList.get(info.position).getPath());
                editor.putStringSet("songs", set);
                editor.apply();
                songList.remove(info.position);
                songListAdapter.setSongList(songList);
                songListView.setAdapter(songListAdapter);
                return true;
            case R.id.rename:
                return true;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
}
