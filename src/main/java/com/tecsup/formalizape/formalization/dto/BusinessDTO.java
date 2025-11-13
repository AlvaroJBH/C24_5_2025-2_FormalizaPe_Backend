package com.tecsup.formalizape.formalization.dto;

import lombok.*;
import java.util.Set;
import com.tecsup.formalizape.common.enums.ProgressStatus;

public class BusinessDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RequestDTO {
        private String name;
        private String description;
        private String sector;
        private String status;
        private Long ownerId;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ResponseDTO {
        private Long id;
        private String name;
        private String description;
        private String sector;
        private String status;
        private Long ownerId;
        private String ownerUsername;
        private Set<BusinessProcedureDTO.SummaryDTO> procedures;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SummaryDTO {
        private Long id;
        private String name;
        private ProgressStatus status;
    }
}
