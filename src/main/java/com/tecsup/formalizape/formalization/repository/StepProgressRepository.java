package com.tecsup.formalizape.formalization.repository;

import com.tecsup.formalizape.formalization.model.StepProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepProgressRepository extends JpaRepository<StepProgress, Long> {

    List<StepProgress> findByBusinessProcedureId(Long businessProcedureId);

    List<StepProgress> findByProcedureStepId(Long procedureStepId);
}
