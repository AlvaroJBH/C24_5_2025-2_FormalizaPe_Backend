package com.tecsup.formalizape.auth.model;

import com.tecsup.formalizape.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "users")
public class User extends BaseEntity {

    // Nombre de la empresa que se usará como username
    @Column(nullable = false, unique = true)
    private String username;

    // Correo electrónico obligatorio y único
    @Column(nullable = false, unique = true)
    private String email;

    // Contraseña
    @Column(nullable = false)
    private String password;

    // DNI opcional
    private String dni;

    // RUC opcional
    private String ruc;

    @Builder.Default
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
