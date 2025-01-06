/**
 * 자카
 */
package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.AddWatchListRequest;
import com.crazy.filmzip.dto.WatchListResponse;
import com.crazy.filmzip.entity.ToWatchMovie;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.service.UserService;
import com.crazy.filmzip.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/watchList")
public class WatchListApiController {

    private final WatchListService watchListService;
    private final UserService userService;
    Long userID = null;

    @PostMapping("")
    public ResponseEntity<WatchListResponse> addMovieToWatchList(@RequestBody AddWatchListRequest request) {

        User user = getCurrentUser();
        userID = user.getId();
        // Set a test user ID for now (replace with real user authentication later)
        request.setUserId(user.getId());
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
//        watchListService.deleteMovieFromWatchList(movieApiId);
        watchListService.deleteMovieFromWatchList(userID, movieApiId);
        System.out.println("MovieID in controller - ");
        System.out.println(movieApiId);
        return ResponseEntity.ok().build();
    };

    // 현재 인증된 사용자 가져오기
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 사용자 인증 정보가 없습니다.");
        }

        String email = authentication.getName();
        System.out.println("Authenticated User Email: " + email);

        return userService.findByEmail(email);
    }
}
