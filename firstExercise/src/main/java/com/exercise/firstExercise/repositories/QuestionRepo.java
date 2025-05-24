package com.exercise.firstExercise.repositories;

import com.exercise.firstExercise.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {
}
