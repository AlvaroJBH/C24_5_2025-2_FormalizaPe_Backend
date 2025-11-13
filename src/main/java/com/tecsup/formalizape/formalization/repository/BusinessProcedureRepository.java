package com.tecsup.formalizape.formalization.repository;

import com.tecsup.formalizape.formalization.model.BusinessProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessProcedureRepository extends JpaRepository<BusinessProcedure, Long> {

    List<BusinessProcedure> findByBusinessId(Long businessId);

    List<BusinessProcedure> findByProcedureId(Long procedureId);
}
