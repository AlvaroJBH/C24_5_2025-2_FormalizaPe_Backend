package com.tecsup.formalizape.formalization.dto;

import lombok.*;
import java.util.Set;
import com.tecsup.formalizape.common.enums.ProgressStatus;

public class BusinessProcedureDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RequestDTO {
        private Long businessId;
        private Long procedureId;
        private ProgressStatus status; // opcional
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ResponseDTO {
        private Long id;
        private ProgressStatus status;
        private Long businessId;
        private Long procedureId;
        private String procedureName;
        private Set<StepProgressDTO.SummaryDTO> steps;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SummaryDTO {
        private Long id;
        private String procedureName;
        private ProgressStatus status;
    }
}
