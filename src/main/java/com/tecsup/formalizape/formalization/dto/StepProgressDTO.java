package com.tecsup.formalizape.formalization.dto;

import lombok.*;
import com.tecsup.formalizape.common.enums.ProgressStatus;

public class StepProgressDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RequestDTO {
        private Long businessProcedureId;
        private Long procedureStepId;
        private ProgressStatus status;
        private String notes;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ResponseDTO {
        private Long id;
        private ProgressStatus status;
        private String notes;
        private Long businessProcedureId;
        private Long procedureStepId;
        private String procedureStepTitle;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SummaryDTO {
        private Long id;
        private ProgressStatus status;
    }
}
