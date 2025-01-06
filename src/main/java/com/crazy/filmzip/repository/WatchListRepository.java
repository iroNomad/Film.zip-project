/**
 * 자카
 */
package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.ToWatchMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchListRepository extends JpaRepository<ToWatchMovie, Number> {

    List<ToWatchMovie> findByUserIdOrderByAddedAtDesc(Long id);
    boolean existsByUserIdAndMovieApiId(Long userId, Integer movieId);
    void deleteByUserIdAndMovieApiId(Long userId, Integer movieId);
}
