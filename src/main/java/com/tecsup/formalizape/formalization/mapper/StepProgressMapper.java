package com.tecsup.formalizape.formalization.mapper;

import com.tecsup.formalizape.formalization.dto.StepProgressDTO;
import com.tecsup.formalizape.formalization.model.StepProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StepProgressMapper {

    @Mapping(target = "businessProcedure", ignore = true) // resolver en service
    @Mapping(target = "procedureStep", ignore = true)     // resolver en service
    StepProgress toEntity(StepProgressDTO.RequestDTO dto);

    @Mapping(source = "procedureStep.id", target = "procedureStepId")
    @Mapping(source = "procedureStep.title", target = "procedureStepTitle")
    @Mapping(source = "businessProcedure.id", target = "businessProcedureId")
    StepProgressDTO.ResponseDTO toResponse(StepProgress progress);

    StepProgressDTO.SummaryDTO toSummary(StepProgress progress);
}
