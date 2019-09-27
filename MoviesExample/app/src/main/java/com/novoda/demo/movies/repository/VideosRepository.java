package com.novoda.demo.movies.repository;

public class VideosRepository {
    public void store(java.util.List<com.novoda.demo.movies.model.Video> videos) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
