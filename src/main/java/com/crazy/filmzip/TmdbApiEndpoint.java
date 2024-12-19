package com.crazy.filmzip;

public enum TmdbApiEndpoint {
    TRENDING("trending/movie/day"),
    SEARCH("search/movie"),
    DETAIL("movie/");

    private final String path;
    TmdbApiEndpoint(String path) {
        this.path = path;
    }

    public String getFullUrl() {
        return "https://api.themoviedb.org/3/" + path;
    }
}

