package com.tecsup.formalizape.formalization.repository;

import com.tecsup.formalizape.formalization.model.ProcedureStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcedureStepRepository extends JpaRepository<ProcedureStep, Long> {

    List<ProcedureStep> findByProcedureId(Long procedureId);
}
