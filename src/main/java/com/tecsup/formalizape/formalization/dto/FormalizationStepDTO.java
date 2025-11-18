package com.tecsup.formalizape.formalization.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FormalizationStepDTO {
    private Long stepId;
    private String title;
    private String description;
    private Integer stepOrder;

    private String status;
    private String notes;
}
