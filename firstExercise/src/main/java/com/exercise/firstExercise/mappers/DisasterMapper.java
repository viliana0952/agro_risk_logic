package com.exercise.firstExercise.mappers;

import com.exercise.firstExercise.dto.DisasterDTO;
import com.exercise.firstExercise.dto.QuestionDTO;
import com.exercise.firstExercise.models.NaturalDisaster;
import com.exercise.firstExercise.models.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DisasterMapper {

    DisasterDTO toDto(NaturalDisaster entity);

    NaturalDisaster toEntity(DisasterDTO dto);

//    @Mapping(target = "naturalDisaster", source = "dto.naturalDisaster")
//    NaturalDisaster convertDtoToEntity(DisasterDTO dto, Long id);
//
//    @Mapping(target = "naturalDisaster", source = "entity.naturalDisaster")
//    DisasterDTO convertEntityToDto(NaturalDisaster entity);
}
