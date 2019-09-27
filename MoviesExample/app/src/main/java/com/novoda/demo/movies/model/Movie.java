package com.novoda.demo.movies.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    public String id;
    public String title;
    public String poster_path;
    public String backdrop_path;

    @SerializedName("vote_average")
    public double rating;

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", rating=" + rating +
                '}';
    }

    public String posterUrl() {
        return "http://image.tmdb.org/t/p/w92" + poster_path;
    }

    public String backDropUrl() {
        return "http://image.tmdb.org/t/p/w300" + poster_path;
    }
}
