package com.novoda.demo.movies.model;

public class Video {

    public String id;
    public String key;
    public String site;
    public String type;

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", site='" + site + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String trailerUrl() {
        if (!"YouTube".equals(site) || !"Trailer".equals(type)) {
            return null;
        }
        return "https://www.youtube.com/watch?v=" + key;
    }
}
