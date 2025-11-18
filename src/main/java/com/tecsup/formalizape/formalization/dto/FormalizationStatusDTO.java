package com.tecsup.formalizape.formalization.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FormalizationStatusDTO {
    private Long businessId;
    private String businessName;
    private List<FormalizationProcedureDTO> procedures;
}