package com.novoda.demo.movies;

import com.novoda.demo.movies.model.Movie;

import java.util.List;

public class MoviesSate {

    private final List<Movie> movies;
    private final int pageNumber;

    public MoviesSate(List<Movie> movies, int pageNumber) {
        this.movies = movies;
        this.pageNumber = pageNumber;
    }

    public List<Movie> movies() {
        return movies;
    }

    public int pageNumber() {
        return pageNumber;
    }

    public boolean isEmpty() {
        return movies.isEmpty();
    }

    public int size() {
        return movies.size();
    }

    public Movie get(int position) {
        return movies.get(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MoviesSate that = (MoviesSate) o;

        if (pageNumber != that.pageNumber) {
            return false;
        }
        return movies != null ? movies.equals(that.movies) : that.movies == null;

    }

    @Override
    public int hashCode() {
        int result = movies != null ? movies.hashCode() : 0;
        result = 31 * result + pageNumber;
        return result;
    }
}
