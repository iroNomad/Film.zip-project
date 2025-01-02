package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.ToWatchMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchListRepository extends JpaRepository<ToWatchMovie, Long> {

    List<ToWatchMovie> findByUserId(Long id);
}
