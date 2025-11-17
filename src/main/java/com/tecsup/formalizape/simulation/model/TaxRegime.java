package com.tecsup.formalizape.simulation.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tax_regimes")
public class TaxRegime extends BaseEntity {

    // Código del régimen (RUS, RER, MYPE, GENERAL, etc.)
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    // Nombre completo del régimen
    @Column(nullable = false, length = 150)
    private String name;

    // Descripción general del régimen
    @Column(columnDefinition = "TEXT")
    private String description;

    // JSON opcional para reglas dinámicas o parámetros futuros
    @Column(name = "rules_json", columnDefinition = "TEXT")
    private String rulesJson;
}
