package com.tecsup.formalizape.formalization.dto;

import lombok.Data;

@Data
public class FormalizationToggleStepRequest {
    private Long businessId;
    private Long procedureId;
    private Long stepId;
    private boolean completed;
    private String notes;
}
