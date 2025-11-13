package com.tecsup.formalizape.formalization.repository;

import com.tecsup.formalizape.formalization.model.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {
}
