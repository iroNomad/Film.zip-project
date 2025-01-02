package com.crazy.filmzip.dto;

import com.crazy.filmzip.entity.ToWatchMovie;
import com.crazy.filmzip.entity.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WatchListResponse {
    private String title;
    private int movieApiId;  // Movie ID associated with the entry

    // Constructor to populate the response object from the entity
    public WatchListResponse(ToWatchMovie watchList) {
        this.title = watchList.getTitle();
        this.movieApiId = watchList.getMovieApiId();
    }
}
