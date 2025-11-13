package com.tecsup.formalizape.formalization.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import com.tecsup.formalizape.common.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "step_progress")
public class StepProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_procedure_id")
    private BusinessProcedure businessProcedure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_step_id")
    private ProcedureStep procedureStep;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status = ProgressStatus.PENDING;

    @Column(length = 500)
    private String notes;
}
