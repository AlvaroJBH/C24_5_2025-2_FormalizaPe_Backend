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

    // Relación con la simulación que generó este resultado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulation_input_id", nullable = false)
    private SimulationInput simulationInput;

    // Relación con el régimen tributario evaluado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_regime_id", nullable = false)
    private TaxRegime taxRegime;

    // Impuesto mensual calculado
    @Column(name = "monthly_tax", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyTax;

    // IGV mensual calculado
    @Column(name = "monthly_igv", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyIgv;

    // Total mensual (impuesto + IGV)
    @Column(name = "total_monthly", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalMonthly;

    // Total anual (multiplicado por 12)
    @Column(name = "total_annual", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAnnual;

    // Indica si este régimen fue el recomendado
    @Column(nullable = false)
    private Boolean recommended;

    // Información adicional opcional (beneficios, detalles, notas)
    @Column(name = "details_json", columnDefinition = "TEXT")
    private String detailsJson;
}
