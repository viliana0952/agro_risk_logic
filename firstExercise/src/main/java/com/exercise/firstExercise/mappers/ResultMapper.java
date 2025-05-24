package com.exercise.firstExercise.mappers;

import com.exercise.firstExercise.dto.QuestionDTO;
import com.exercise.firstExercise.dto.ResultDTO;
import com.exercise.firstExercise.models.Question;
import com.exercise.firstExercise.models.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResultMapper {

    ResultDTO toDto(Result entity);

    Result toEntity(ResultDTO dto);

//    @Mapping(target = "possibilities", source = "dto.possibilities")
//    Result convertDtoToEntity(ResultDTO dto, Long id);
//
//    @Mapping(target = "possibilities", source = "entity.possibilities")
//    ResultDTO convertEntityToDto(Result entity);
}
