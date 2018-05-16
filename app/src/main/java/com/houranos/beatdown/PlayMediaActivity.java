package com.houranos.beatdown;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayMediaActivity extends Activity implements SurfaceHolder.Callback {
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    SeekBar playbackRate;
    Handler handler;
    FloatingActionButton hideFab;
    FloatingActionButton revealFab;
    FloatingActionButton loopFab;
    FloatingActionButton closeFab;
    int loopFlag = 0;
    int loopStartPos;
    int loopEndPos;
    TextView speedText;
    Group groupView;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_media_player);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        surfaceView = (SurfaceView) findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        playbackRate = (SeekBar) findViewById(R.id.playspeed);
        seekBar.setClickable(false);
        handler = new Handler();
        groupView = (Group) findViewById(R.id.menu);
        hideFab = (FloatingActionButton) findViewById(R.id.hideButton);
        hideFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupView.setVisibility(View.GONE);
                revealFab.setVisibility(View.VISIBLE);
            }
        });
        revealFab = (FloatingActionButton) findViewById(R.id.revealButton);
        revealFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupView.setVisibility(View.VISIBLE);
                revealFab.setVisibility(View.GONE);
            }
        });
        loopFab = (FloatingActionButton) findViewById(R.id.loopButton);
        loopFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (loopFlag) {
                    case 0:
                        loopStartPos = mediaPlayer.getCurrentPosition();
                        Toast.makeText(PlayMediaActivity.this, "Loop Start", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        loopEndPos = mediaPlayer.getCurrentPosition();
                        Toast.makeText(PlayMediaActivity.this, "Loop End", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        loopStartPos = 0;
                        loopEndPos = mediaPlayer.getDuration();
                        Toast.makeText(PlayMediaActivity.this, "Loop Clear", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                loopFlag++;
                loopFlag %= 3;
            }
        });
        closeFab = (FloatingActionButton) findViewById(R.id.closeButton);
        closeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });
        speedText = (TextView) findViewById(R.id.speedText);
        speedText.setText("100");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Intent intent = getIntent();
        try {
            mediaPlayer = MediaPlayer.create(this, Uri.parse(intent.getStringExtra("FILEPATH")));

        } catch (Exception e) {
            System.out.println("Opening file failed");
        }
        loopStartPos = 0;
        loopEndPos = mediaPlayer.getDuration();

        mediaPlayer.setDisplay(holder);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    if (currentPosition > loopEndPos) {
                        currentPosition = loopStartPos;
                        mediaPlayer.seekTo(currentPosition);
                    }
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b) {
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });
        playbackRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedText.setText(String.valueOf(progress + 10));
                PlaybackParams params = mediaPlayer.getPlaybackParams();
                params.setSpeed((progress + 10) / 100f);
                mediaPlayer.setPlaybackParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
