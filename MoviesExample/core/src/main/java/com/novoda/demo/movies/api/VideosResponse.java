package com.novoda.demo.movies.api;

import com.novoda.demo.movies.model.Video;

import java.util.List;

public class VideosResponse {

    public String id;

    public List<Video> results;

    @Override
    public String toString() {
        return "VideosResponse{" +
                "id=" + id +
                ", results=" + results +
                '}';
    }
}
