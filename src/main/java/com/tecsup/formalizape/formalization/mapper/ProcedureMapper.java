package com.tecsup.formalizape.formalization.mapper;

import com.tecsup.formalizape.formalization.dto.ProcedureDTO;
import com.tecsup.formalizape.formalization.model.Procedure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProcedureMapper {

    Procedure toEntity(ProcedureDTO.RequestDTO dto);

    @Mapping(source = "steps", target = "steps") // MapStruct mapea colección automáticamente
    ProcedureDTO.ResponseDTO toResponse(Procedure procedure);
}
