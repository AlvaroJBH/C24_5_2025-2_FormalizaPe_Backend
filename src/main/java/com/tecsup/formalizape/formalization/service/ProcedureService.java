package com.tecsup.formalizape.formalization.service;

import com.tecsup.formalizape.formalization.model.Procedure;
import com.tecsup.formalizape.formalization.repository.ProcedureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcedureService {

    private final ProcedureRepository procedureRepository;

    public List<Procedure> getAll() {
        return procedureRepository.findAll();
    }

    public Procedure getById(Long id) {
        return procedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedure not found"));
    }

    public Procedure create(Procedure procedure) {
        return procedureRepository.save(procedure);
    }

    public Procedure update(Long id, Procedure procedure) {
        Procedure existing = getById(id);
        existing.setName(procedure.getName());
        existing.setDescription(procedure.getDescription());
        return procedureRepository.save(existing);
    }

    public void delete(Long id) {
        procedureRepository.deleteById(id);
    }
}
