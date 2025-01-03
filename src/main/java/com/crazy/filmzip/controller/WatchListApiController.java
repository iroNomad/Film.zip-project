package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.AddWatchListRequest;
import com.crazy.filmzip.dto.WatchListResponse;
import com.crazy.filmzip.entity.ToWatchMovie;
import com.crazy.filmzip.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/watchList")
public class WatchListApiController {

    private final WatchListService watchListService;

    @PostMapping("")
    public ResponseEntity<WatchListResponse> addMovieToWatchList(@RequestBody AddWatchListRequest request) {
        // Set a test user ID for now (replace with real user authentication later)
        request.setUserId(1L);
        System.out.println("movieApiId: " + request.getMovieApiId());
        System.out.println("Request ------> " + request.toString());
        // Save the movie to the watch list using a service
        ToWatchMovie savedWatchList = watchListService.save(request);

        // Return a response with the saved watch list details
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new WatchListResponse(savedWatchList));
    }

    @DeleteMapping("/{movieApiId}")
    public ResponseEntity<Void> deleteMovieFromWatchList(@PathVariable Integer movieApiId) {
        watchListService.deleteMovieFromWatchList(movieApiId);
        System.out.println("MovieID in controller - ");
        System.out.println(movieApiId);
        return ResponseEntity.ok().build();
    };
}
