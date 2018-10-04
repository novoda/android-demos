package com.jetlightstudio.jettunes;

import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

public class SongActivity extends AppCompatActivity {
    TextView songTitle;
    TextView currentTime;
    TextView songDuartion;
    TextView songIndex;
    Button muteButton;
    Button playButton;
    Button shuffleButton;
    Button repeatButton;

    SeekBar seekBar;
    final Handler handler = new Handler();
    Runnable runnable;

    Uri currentURI;
    int currentIndex = 0;
    boolean shuffle = true;
    boolean repeatAll = true;
    boolean mute = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        songTitle = (TextView) findViewById(R.id.songTitle);
        songIndex = (TextView) findViewById(R.id.songIndex);
        muteButton = (Button) findViewById(R.id.mute);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        currentTime = (TextView) findViewById(R.id.currentTime);
        songDuartion = (TextView) findViewById(R.id.duration);
        repeatButton = (Button) findViewById(R.id.repeat);
        shuffleButton = (Button) findViewById(R.id.shuffle);
        playButton = (Button) findViewById(R.id.play);

        loadData();
        settingUpSeekBar();
    }

    public void setCurrentURI(int index) {
        int i = MainActivity.songsMap.size() - 1 - index;
        String Title = MainActivity.songsMap.get(i).getSongTitle();
        currentURI = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MainActivity.songsMap.get(i).getmSongID());
        currentIndex = index;
        songIndex.setText((currentIndex + 1) + "/" + MainActivity.songsMap.size());
        songTitle.setText(Title);
        long total = Long.valueOf(MainActivity.songsMap.get(MainActivity.songsMap.size() - 1 - currentIndex).getDuration());
        int min = (int) total / 60000;
        int sec = (int) total / 1000 % 60;
        songDuartion.setText(String.format("%02d:%02d", min, sec));
        songIndex.setText((currentIndex + 1) + "/" + MainActivity.songsMap.size());
    }

    public void startMusic(final View v) {
        MainActivity.mp.stop();
        MainActivity.mp = MediaPlayer.create(this, currentURI);
        playButton.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_24dp);
        if (MainActivity.mp != null) {
            MainActivity.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (repeatAll) {
                        if (currentIndex >= MainActivity.songsMap.size() - 1) {
                            currentIndex = 0;
                            startMusic(v);
                        } else {
                            nextSong(v);
                        }
                    } else {
                        startMusic(v);
                    }
                }
            });
            MainActivity.mp.start();
        }
    }

    public void nextSong(View view) {
        setCurrentURI(!shuffle ? currentIndex + 1 : new Random().nextInt(MainActivity.songsMap.size()));
        startMusic(view);
    }

    public void prevSong(View view) {
        setCurrentURI(!shuffle ? currentIndex - 1 : new Random().nextInt(MainActivity.songsMap.size()));
        startMusic(view);
    }

    public void shuffleSong(View view) {
        shuffle = !shuffle;
        Toast.makeText(this, shuffle ? "Shuffle On" : "Shuffle Off", Toast.LENGTH_SHORT).show();
        shuffleButton.setBackgroundResource(shuffle ? R.drawable.ic_shuffle_white_24dp : R.drawable.ic_shuffle_black_24dp);
    }

    public void repeatSong(View view) {
        repeatAll = !repeatAll;
        repeatButton.setBackgroundResource(repeatAll ? R.drawable.ic_repeat_white_24dp : R.drawable.ic_repeat_one_white_24dp);
    }

    public void playMusic(View view) {
        if (MainActivity.mp != null) {
            if (MainActivity.mp.isPlaying()) {
                MainActivity.mp.pause();
                handler.removeCallbacks(runnable);
                playButton.setBackgroundResource(R.drawable.ic_play_circle_outline_white_24dp);
            } else {
                MainActivity.mp.start();
                handler.postDelayed(runnable, 0);
                playButton.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_24dp);
            }
        }
    }

    public void loadData() {
        currentIndex = getIntent().getExtras().getInt("currentIndex");
        shuffle = getIntent().getExtras().getBoolean("shuffle");
        repeatAll = getIntent().getExtras().getBoolean("repeatAll");
        songTitle.setText(MainActivity.songsMap.get(MainActivity.songsMap.size() - 1 - currentIndex).getSongTitle());
        long total = Long.valueOf(MainActivity.songsMap.get(MainActivity.songsMap.size() - 1 - currentIndex).getDuration());
        int min = (int) total / 60000;
        int sec = (int) total / 1000 % 60;
        songDuartion.setText(String.format("%02d:%02d", min, sec));
        seekBar.setMax(MainActivity.mp.getDuration());
        songIndex.setText((currentIndex + 1) + "/" + MainActivity.songsMap.size());
    }

    public void goToList(View view) {
        onBackPressed();
    }

    public void muteSong(View view) {
        mute = !mute;
        muteButton.setBackgroundResource(mute ? R.drawable.ic_volume_off_white_24dp : R.drawable.ic_volume_up_white_24dp);
        MainActivity.mp.setVolume(mute ? 0 : 1, mute ? 0 : 1);
    }

    public void settingUpSeekBar() {
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(MainActivity.mp.getCurrentPosition());
                int total = MainActivity.mp.getCurrentPosition();
                int min = total / 60000;
                int sec = total / 1000 % 60;
                currentTime.setText(String.format("%02d:%02d", min, sec));
                handler.postDelayed(this, 50);
            }
        };

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    MainActivity.mp.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler.postDelayed(runnable, 0);
    }
}
