/**
 * 자카
 */
package com.crazy.filmzip.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Movie {

    String imgBaseUrl = "https://image.tmdb.org/t/p/original";

    @JsonProperty("adult")
    private boolean adult;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("budget")
    private long budget;

    @JsonProperty("homepage")
    private String homepage;

    @JsonProperty("id")
    private int id;

    @JsonProperty("imdb_id")
    private String imdbId;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("popularity")
    private double popularity;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("revenue")
    private long revenue;

    @JsonProperty("runtime")
    private int runtime;

    @JsonProperty("status")
    private String status;

    @JsonProperty("tagline")
    private String tagline;

    @JsonProperty("title")
    private String title;

    @JsonProperty("vote_average")
    private double voteAverage;

    @Setter
    private String rating;

    @JsonProperty("vote_count")
    private long voteCountRaw;

    @Setter
    private String voteCount;

    // Nested objects
    public static class Collection {
        @JsonProperty("id")
        private int id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("backdrop_path")
        private String backdropPath;
    }

    @JsonProperty("belongs_to_collection")
    private Collection belongsToCollection;

    @Getter
    public static class Genre {
        @JsonProperty("id")
        private int id;

        @JsonProperty("name")
        private String name;
    }

    @JsonProperty("genres")
    private List<Genre> genres;

    public static class ProductionCompany {
        @JsonProperty("id")
        private int id;

        @JsonProperty("logo_path")
        private String logoPath;

        @JsonProperty("name")
        private String name;

        @JsonProperty("origin_country")
        private String originCountry;
    }

    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies;

    public static class Country {
        @JsonProperty("iso_3166_1")
        private String iso3166_1;

        @JsonProperty("name")
        private String name;
    }

    @JsonProperty("production_countries")
    private List<Country> productionCountries;

    public static class Language {
        @JsonProperty("english_name")
        private String englishName;

        @JsonProperty("iso_639_1")
        private String iso639_1;

        @JsonProperty("name")
        private String name;
    }

    @JsonProperty("spoken_languages")
    private List<Language> spokenLanguages;

    public String getPosterPath() {
        return imgBaseUrl + posterPath;
    }

    public String getBackdropPath() {
        return imgBaseUrl + backdropPath;
    }


}
