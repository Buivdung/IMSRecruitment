package com.hust.interviewmanagement.repository;

import com.hust.interviewmanagement.entities.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findAllByName(String name);
}
