package com.tecsup.formalizape.formalization.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import com.tecsup.formalizape.common.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "business_procedures")
public class BusinessProcedure extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status = ProgressStatus.PENDING;

    @OneToMany(mappedBy = "businessProcedure", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StepProgress> stepProgresses;
}
