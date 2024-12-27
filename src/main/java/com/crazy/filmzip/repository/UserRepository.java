package com.crazy.filmzip.repository;

import com.crazy.filmzip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
