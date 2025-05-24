package com.exercise.firstExercise.repositories;

import com.exercise.firstExercise.models.NaturalDisaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisasterRepo extends JpaRepository<NaturalDisaster, Long> {
}
