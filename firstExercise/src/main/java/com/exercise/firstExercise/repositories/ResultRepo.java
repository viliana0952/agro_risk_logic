package com.exercise.firstExercise.repositories;

import com.exercise.firstExercise.models.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepo extends JpaRepository<Result, Long> {
}
