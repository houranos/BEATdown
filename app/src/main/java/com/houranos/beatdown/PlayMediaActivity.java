package com.houranos.beatdown;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class PlayMediaActivity extends Activity implements SurfaceHolder.Callback {
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    MediaPlayer mMediaPlayer;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_media_player);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Intent intent = getIntent();
            mMediaPlayer = MediaPlayer.create(this, Uri.parse(intent.getStringExtra("FILEPATH")));
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.start();
        } catch (Exception e) {
            System.out.println("Opening file failed");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
