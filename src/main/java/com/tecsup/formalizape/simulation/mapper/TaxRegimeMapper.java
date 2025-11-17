package com.tecsup.formalizape.simulation.mapper;

import com.tecsup.formalizape.simulation.dto.TaxRegimeResponse;
import com.tecsup.formalizape.simulation.model.TaxRegime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaxRegimeMapper {

    TaxRegimeResponse toResponse(TaxRegime regime);
}
