package com.tecsup.formalizape.simulation.dto;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SimulationResultResponse {

    private Long simulationInputId;
    private Long recommendedResultId;

    private List<SimulationResultItem> results;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SimulationResultItem {
        private Long resultId;
        private String regimeCode;
        private String regimeName;

        private String monthlyTax;
        private String monthlyIgv;
        private String totalMonthly;
        private String totalAnnual;

        private Boolean recommended;
    }
}
