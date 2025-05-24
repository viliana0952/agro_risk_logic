package com.exercise.firstExercise.dto;

import com.exercise.firstExercise.models.Disaster;
import com.exercise.firstExercise.models.NaturalDisaster;

public record QuestionDTO(String title, Disaster disaster, int options /*NaturalDisaster naturalDisaster*/) {
}
