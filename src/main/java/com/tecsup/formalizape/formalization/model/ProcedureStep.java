package com.tecsup.formalizape.formalization.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "procedure_steps")
public class ProcedureStep extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private Integer stepOrder; // e.g. 1, 2, 3...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @OneToMany(mappedBy = "procedureStep", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StepProgress> stepProgresses;
}
