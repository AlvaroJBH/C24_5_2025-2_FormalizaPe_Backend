package com.tecsup.formalizape.auth.repository;

import com.tecsup.formalizape.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // para login
    boolean existsByEmail(String email);     // validar registro
    boolean existsByUsername(String username); // validar nombre de empresa único
}
