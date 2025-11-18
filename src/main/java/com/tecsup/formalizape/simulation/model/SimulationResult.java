package com.tecsup.formalizape.simulation.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "simulation_results")
public class SimulationResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulation_input_id", nullable = false)
    private SimulationInput simulationInput;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_regime_id", nullable = false)
    private TaxRegime taxRegime;

    // si el régimen NO está disponible → estos valores serán null
    @Column(name = "monthly_tax", precision = 12, scale = 2)
    private BigDecimal monthlyTax;

    @Column(name = "monthly_igv", precision = 12, scale = 2)
    private BigDecimal monthlyIgv;

    @Column(name = "total_monthly", precision = 12, scale = 2)
    private BigDecimal totalMonthly;

    @Column(name = "total_annual", precision = 12, scale = 2)
    private BigDecimal totalAnnual;

    // Nuevo campo: indica si este régimen aplica o no
    @Column(nullable = false)
    private Boolean available;

    @Column(nullable = false)
    private Boolean recommended;

    @Column(name = "details_json", columnDefinition = "TEXT")
    private String detailsJson;
}
