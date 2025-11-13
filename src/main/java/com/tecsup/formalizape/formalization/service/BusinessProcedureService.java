package com.tecsup.formalizape.formalization.service;

import com.tecsup.formalizape.formalization.model.BusinessProcedure;
import com.tecsup.formalizape.formalization.repository.BusinessProcedureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessProcedureService {

    private final BusinessProcedureRepository businessProcedureRepository;

    public List<BusinessProcedure> getAll() {
        return businessProcedureRepository.findAll();
    }

    public BusinessProcedure getById(Long id) {
        return businessProcedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BusinessProcedure not found"));
    }

    public List<BusinessProcedure> getByBusinessId(Long businessId) {
        return businessProcedureRepository.findByBusinessId(businessId);
    }

    public BusinessProcedure create(BusinessProcedure bp) {
        return businessProcedureRepository.save(bp);
    }

    public BusinessProcedure update(Long id, BusinessProcedure bp) {
        BusinessProcedure existing = getById(id);
        existing.setStatus(bp.getStatus());
        return businessProcedureRepository.save(existing);
    }

    public void delete(Long id) {
        businessProcedureRepository.deleteById(id);
    }
}
