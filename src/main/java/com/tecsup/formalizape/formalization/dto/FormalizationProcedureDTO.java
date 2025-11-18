package com.tecsup.formalizape.formalization.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FormalizationProcedureDTO {
    private Long procedureId;
    private String name;
    private String description;
    private String category;
    private String status;
    private int completedSteps;
    private int totalSteps;
    private double progressPercent;
    private List<FormalizationStepDTO> steps;
}
