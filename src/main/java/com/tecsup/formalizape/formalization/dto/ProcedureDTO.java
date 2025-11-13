package com.tecsup.formalizape.formalization.dto;

import lombok.*;
import java.util.Set;

public class ProcedureDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RequestDTO {
        private String name;
        private String description;
        private String category;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ResponseDTO {
        private Long id;
        private String name;
        private String description;
        private String category;
        private Set<ProcedureStepDTO.ResponseDTO> steps;
    }
}
