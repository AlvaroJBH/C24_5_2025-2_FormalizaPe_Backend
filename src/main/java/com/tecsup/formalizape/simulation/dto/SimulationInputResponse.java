package com.tecsup.formalizape.simulation.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SimulationInputResponse {

    private Long id;
    private Long businessId;
    private Integer versionNumber;

    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;
    private Integer quantity;
    private String type;
    private BigDecimal assets;
}
