package com.tecsup.formalizape.formalization.service;

import com.tecsup.formalizape.formalization.model.ProcedureStep;
import com.tecsup.formalizape.formalization.repository.ProcedureStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcedureStepService {

    private final ProcedureStepRepository procedureStepRepository;

    public List<ProcedureStep> getAll() {
        return procedureStepRepository.findAll();
    }

    public ProcedureStep getById(Long id) {
        return procedureStepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProcedureStep not found"));
    }

    public List<ProcedureStep> getByProcedureId(Long procedureId) {
        return procedureStepRepository.findByProcedureId(procedureId);
    }

    public ProcedureStep create(ProcedureStep step) {
        return procedureStepRepository.save(step);
    }

    public ProcedureStep update(Long id, ProcedureStep step) {
        ProcedureStep existing = getById(id);
        existing.setTitle(step.getTitle());
        existing.setDescription(step.getDescription());
        existing.setStepOrder(step.getStepOrder());
        return procedureStepRepository.save(existing);
    }

    public void delete(Long id) {
        procedureStepRepository.deleteById(id);
    }
}
