package com.jetlightstudio.jettunes;

import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by oussama on 24/11/2017.
 */

public class Song {
    private long mSongID;
    private String mSongTitle;
    private String mSongAlbum;
    private String duration;
    private int idAlbum;

    public Song(long mSongID, String mSongTitle, String mSongAlbum, String duration, int idAlbum) {

        this.mSongID = mSongID;
        this.mSongTitle = mSongTitle;
        this.mSongAlbum = mSongAlbum;
        this.duration = duration;
        this.idAlbum = idAlbum;
    }

    public String getDuration() {
        return duration;
    }

    public long getmSongID() {
        return mSongID;
    }

    public String getmSongAlbum() {
        return mSongAlbum;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public String getSongTitle() {
        return mSongTitle;
    }
}
