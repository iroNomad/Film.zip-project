package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.ToWatchMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchListRepository extends JpaRepository<ToWatchMovie, Number> {

    boolean existsByMovieApiId(Integer movieId);
    List<ToWatchMovie> findByMovieApiId(Integer movieId);
    List<ToWatchMovie> findByUserIdOrderByAddedAtDesc(Long id);
    void deleteByMovieApiId(Integer movieId);
}
