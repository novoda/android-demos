package com.novoda.demo.movies.api;

import com.novoda.demo.movies.model.Movie;

import java.util.List;

public class MoviesResponse {

    public int page;

    public List<Movie> results;

    public int total_pages;

    public int total_results;

    public List<Movie> results() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public String toString() {
        return "MoviesResponse{" +
                "page=" + page +
                ", total_pages=" + total_pages +
                ", total_results=" + total_results +
                '}';
    }
}
