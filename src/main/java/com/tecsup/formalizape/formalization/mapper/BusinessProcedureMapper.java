package com.tecsup.formalizape.formalization.mapper;

import com.tecsup.formalizape.formalization.dto.BusinessProcedureDTO;
import com.tecsup.formalizape.formalization.model.BusinessProcedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusinessProcedureMapper {

    // RequestDTO -> Entity
    @Mapping(target = "business", ignore = true)   // se resuelve en el service
    @Mapping(target = "procedure", ignore = true) // se resuelve en el service
    @Mapping(target = "stepProgresses", ignore = true)
    BusinessProcedure toEntity(BusinessProcedureDTO.RequestDTO dto);

    // Entity -> ResponseDTO
    @Mapping(source = "procedure.name", target = "procedureName")
    BusinessProcedureDTO.ResponseDTO toResponse(BusinessProcedure bp);

    // Entity -> SummaryDTO
    @Mapping(source = "procedure.name", target = "procedureName")
    BusinessProcedureDTO.SummaryDTO toSummary(BusinessProcedure bp);
}
