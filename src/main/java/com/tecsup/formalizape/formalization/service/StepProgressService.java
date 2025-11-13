package com.tecsup.formalizape.formalization.service;

import com.tecsup.formalizape.formalization.model.StepProgress;
import com.tecsup.formalizape.formalization.repository.StepProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StepProgressService {

    private final StepProgressRepository stepProgressRepository;

    public List<StepProgress> getAll() {
        return stepProgressRepository.findAll();
    }

    public StepProgress getById(Long id) {
        return stepProgressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StepProgress not found"));
    }

    public List<StepProgress> getByBusinessProcedureId(Long businessProcedureId) {
        return stepProgressRepository.findByBusinessProcedureId(businessProcedureId);
    }

    public StepProgress create(StepProgress progress) {
        return stepProgressRepository.save(progress);
    }

    public StepProgress update(Long id, StepProgress progress) {
        StepProgress existing = getById(id);
        existing.setStatus(progress.getStatus());
        existing.setNotes(progress.getNotes());
        return stepProgressRepository.save(existing);
    }

    public void delete(Long id) {
        stepProgressRepository.deleteById(id);
    }
}
