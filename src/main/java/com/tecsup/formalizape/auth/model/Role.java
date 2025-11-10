package com.tecsup.formalizape.auth.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Role extends BaseEntity {
    private String name;
}
