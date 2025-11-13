package com.tecsup.formalizape.formalization.dto;

import lombok.*;
import java.util.Set;

public class ProcedureStepDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RequestDTO {
        private String title;
        private String description;
        private Integer stepOrder;
        private Long procedureId;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ResponseDTO {
        private Long id;
        private String title;
        private String description;
        private Integer stepOrder;
        private Long procedureId;
        private String procedureName;
        private Set<StepProgressDTO.SummaryDTO> progresses;
    }
}
