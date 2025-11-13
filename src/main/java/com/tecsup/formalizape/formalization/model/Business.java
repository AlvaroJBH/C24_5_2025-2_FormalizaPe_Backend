package com.tecsup.formalizape.formalization.model;

import com.tecsup.formalizape.auth.model.User;
import com.tecsup.formalizape.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "businesses")
public class Business extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    private String sector; // e.g. "Food", "Technology", etc.

    private String status; // e.g. "PENDING", "IN_PROGRESS", "COMPLETED"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BusinessProcedure> businessProcedures;
}
