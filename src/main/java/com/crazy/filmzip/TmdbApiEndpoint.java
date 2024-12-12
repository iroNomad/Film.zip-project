package com.crazy.filmzip;

public enum TmdbApiEndpoint {
    TRENDING("trending/movie/day"),
    SEARCH("search/movie");

    private final String path;
    TmdbApiEndpoint(String path) {
        this.path = path;
    }

    public String getFullUrl(String baseUrl) {
        return baseUrl + path;
    }
}

