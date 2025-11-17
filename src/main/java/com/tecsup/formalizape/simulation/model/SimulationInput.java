package com.tecsup.formalizape.simulation.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import com.tecsup.formalizape.formalization.model.Business;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "simulation_inputs")
public class SimulationInput extends BaseEntity {

    // Relación con el negocio dueño de esta simulación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    // Ingresos mensuales ingresados por el usuario
    @Column(name = "monthly_income", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyIncome;

    // Costos mensuales ingresados por el usuario
    @Column(name = "monthly_expense", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyExpense;

    // Cantidad seleccionada (trabajadores/locales/etc según el diseño del sistema)
    @Column(nullable = false)
    private Integer quantity;

    // Tipo de negocio o categoría de actividad
    @Column(nullable = false, length = 100)
    private String type;

    // Valor total de los activos declarados
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal assets;

    // Número de versión por negocio (1, 2, 3, ...)
    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    // Resultados generados por esta simulación
    @OneToMany(mappedBy = "simulationInput", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SimulationResult> results;
}
