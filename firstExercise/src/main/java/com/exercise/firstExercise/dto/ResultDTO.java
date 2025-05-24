package com.exercise.firstExercise.dto;

import com.exercise.firstExercise.models.Disaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ResultDTO(Map<Disaster,Double> possibilities) {
}
