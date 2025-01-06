/**
 * 자카
 */
package com.crazy.filmzip.service;

import com.crazy.filmzip.dto.AddWatchListRequest;
import com.crazy.filmzip.entity.ToWatchMovie;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.UserRepository;
import com.crazy.filmzip.repository.WatchListRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WatchListService {

    private final WatchListRepository watchListRepository;
    private final UserRepository userRepository;

    public ToWatchMovie save(AddWatchListRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + request.getUserId()));

        ToWatchMovie post = request.toEntity(user);
        post.setTitle(request.getTitle());
        post.setBackdropPath(request.getBackdropPath());

        return watchListRepository.save(post);
    }

    public List<ToWatchMovie> getMovieListByUserID(Long id) {

        return watchListRepository.findByUserIdOrderByAddedAtDesc(id);
    }

    @Transactional
    public void deleteMovieFromWatchList(Long userID, Integer movieApiId) {
        System.out.println("UserID and MovieID = " + userID + " " + movieApiId);
        if (watchListRepository.existsByUserIdAndMovieApiId(userID, movieApiId)) {
            watchListRepository.deleteByUserIdAndMovieApiId(userID, movieApiId);
            System.out.println("Movie deleted from watchlist!");
        } else {
            System.out.println("Movie not found in watchlist!");
        }
    };
}
