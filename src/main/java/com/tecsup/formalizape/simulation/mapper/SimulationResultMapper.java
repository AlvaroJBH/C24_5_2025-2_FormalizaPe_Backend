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
    @Mapping(target = "monthlyTax",
            expression = "java(result.getMonthlyTax() != null ? result.getMonthlyTax().toPlainString() : null)")
    @Mapping(target = "monthlyIgv",
            expression = "java(result.getMonthlyIgv() != null ? result.getMonthlyIgv().toPlainString() : null)")
    @Mapping(target = "totalMonthly",
            expression = "java(result.getTotalMonthly() != null ? result.getTotalMonthly().toPlainString() : null)")
    @Mapping(target = "totalAnnual",
            expression = "java(result.getTotalAnnual() != null ? result.getTotalAnnual().toPlainString() : null)")
    @Mapping(source = "recommended", target = "recommended")
    @Mapping(source = "available", target = "available") // nuevo campo
    SimulationResultResponse.SimulationResultItem toItem(SimulationResult result);
}
