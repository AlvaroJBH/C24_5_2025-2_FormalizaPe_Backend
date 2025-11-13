package com.tecsup.formalizape.formalization.mapper;

import com.tecsup.formalizape.formalization.dto.ProcedureStepDTO;
import com.tecsup.formalizape.formalization.model.ProcedureStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProcedureStepMapper {

    @Mapping(target = "procedure", ignore = true) // resolver en service
    @Mapping(target = "stepProgresses", ignore = true)
    ProcedureStep toEntity(ProcedureStepDTO.RequestDTO dto);

    @Mapping(source = "procedure.id", target = "procedureId")
    @Mapping(source = "procedure.name", target = "procedureName")
    ProcedureStepDTO.ResponseDTO toResponse(ProcedureStep step);
}
