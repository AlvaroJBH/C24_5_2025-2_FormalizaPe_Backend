package com.tecsup.formalizape.formalization.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "procedures")
public class Procedure extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    private String category; // e.g. "Legal", "Tax", "Municipal"

    @OneToMany(mappedBy = "procedure", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProcedureStep> steps;

    @OneToMany(mappedBy = "procedure")
    private Set<BusinessProcedure> businessProcedures;
}
