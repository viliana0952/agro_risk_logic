package com.exercise.firstExercise.mappers;

import com.exercise.firstExercise.dto.QuestionDTO;
import com.exercise.firstExercise.dto.ResultDTO;
import com.exercise.firstExercise.models.Question;
import com.exercise.firstExercise.models.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionMapper {

    QuestionDTO toDto(Question entity);

    Question toEntity(QuestionDTO dto);

//    @Mapping(target = "questions", source = "dto.questions")
//    Result convertDtoToEntity(QuestionDTO dto, Long id);
//
//    @Mapping(target = "questions", source = "entity.questions")
//    ResultDTO convertEntityToDto(Question entity);
}
