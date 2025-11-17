package com.tecsup.formalizape.simulation.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class TaxRegimeResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String rulesJson; // si lo deseas exponer
}
