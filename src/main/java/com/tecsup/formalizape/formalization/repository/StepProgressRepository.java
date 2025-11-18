package com.tecsup.formalizape.formalization.repository;

import com.tecsup.formalizape.formalization.model.StepProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StepProgressRepository extends JpaRepository<StepProgress, Long> {

    List<StepProgress> findByBusinessProcedureId(Long businessProcedureId);

    List<StepProgress> findByProcedureStepId(Long procedureStepId);

    Optional<StepProgress> findByBusinessProcedureIdAndProcedureStepId(
            Long businessProcedureId,
            Long procedureStepId
    );
}
