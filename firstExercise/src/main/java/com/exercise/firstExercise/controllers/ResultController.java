package com.exercise.firstExercise.controllers;

import com.exercise.firstExercise.dto.ResultDTO;
import com.exercise.firstExercise.models.Disaster;
import com.exercise.firstExercise.services.ResultService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/risks")
public class ResultController {
    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping("/result")
    public Map<Disaster,Double> getResult(@RequestBody Map<String,Integer> answers){
        var result = resultService.GetAllRisks(answers);
        return result.possibilities();
    }
}
