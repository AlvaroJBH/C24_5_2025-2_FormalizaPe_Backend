package com.tecsup.formalizape.simulation.mapper;

import com.tecsup.formalizape.simulation.dto.SimulationInputRequest;
import com.tecsup.formalizape.simulation.dto.SimulationInputResponse;
import com.tecsup.formalizape.simulation.model.SimulationInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SimulationInputMapper {

    // Request → Entity
    @Mapping(target = "business", ignore = true) // se setea desde el servicio
    @Mapping(target = "versionNumber", ignore = true) // se calcula en service
    @Mapping(target = "results", ignore = true)
    SimulationInput toEntity(SimulationInputRequest dto);

    // Entity → Response
    @Mapping(source = "business.id", target = "businessId")
    SimulationInputResponse toResponse(SimulationInput entity);
}
