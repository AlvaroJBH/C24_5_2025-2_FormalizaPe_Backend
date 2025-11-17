package com.tecsup.formalizape.simulation.mapper;

import com.tecsup.formalizape.simulation.dto.SimulationResultResponse;
import com.tecsup.formalizape.simulation.model.SimulationResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SimulationResultMapper {

    @Mapping(source = "id", target = "resultId")
    @Mapping(source = "taxRegime.code", target = "regimeCode")
    @Mapping(source = "taxRegime.name", target = "regimeName")
    @Mapping(target = "monthlyTax", expression = "java(result.getMonthlyTax().toPlainString())")
    @Mapping(target = "monthlyIgv", expression = "java(result.getMonthlyIgv().toPlainString())")
    @Mapping(target = "totalMonthly", expression = "java(result.getTotalMonthly().toPlainString())")
    @Mapping(target = "totalAnnual", expression = "java(result.getTotalAnnual().toPlainString())")
    SimulationResultResponse.SimulationResultItem toItem(SimulationResult result);
}
