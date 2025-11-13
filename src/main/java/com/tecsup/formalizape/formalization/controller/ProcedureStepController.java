package com.tecsup.formalizape.formalization.controller;

import com.tecsup.formalizape.formalization.dto.ProcedureStepDTO;
import com.tecsup.formalizape.formalization.mapper.ProcedureStepMapper;
import com.tecsup.formalizape.formalization.model.Procedure;
import com.tecsup.formalizape.formalization.model.ProcedureStep;
import com.tecsup.formalizape.formalization.repository.ProcedureRepository;
import com.tecsup.formalizape.formalization.service.ProcedureStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedure-steps")
@RequiredArgsConstructor
public class ProcedureStepController {

    private final ProcedureStepService procedureStepService;
    private final ProcedureStepMapper procedureStepMapper;
    private final ProcedureRepository procedureRepository;

    // 🔹 GET all steps
    @GetMapping
    public ResponseEntity<List<ProcedureStepDTO.ResponseDTO>> getAll() {
        List<ProcedureStep> steps = procedureStepService.getAll();
        List<ProcedureStepDTO.ResponseDTO> response = steps.stream()
                .map(procedureStepMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔹 GET step by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProcedureStepDTO.ResponseDTO> getById(@PathVariable Long id) {
        ProcedureStep step = procedureStepService.getById(id);
        return ResponseEntity.ok(procedureStepMapper.toResponse(step));
    }

    // 🔹 GET steps by Procedure ID
    @GetMapping("/procedure/{procedureId}")
    public ResponseEntity<List<ProcedureStepDTO.ResponseDTO>> getByProcedureId(@PathVariable Long procedureId) {
        List<ProcedureStep> steps = procedureStepService.getByProcedureId(procedureId);
        List<ProcedureStepDTO.ResponseDTO> response = steps.stream()
                .map(procedureStepMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔹 POST create step
    @PostMapping
    public ResponseEntity<ProcedureStepDTO.ResponseDTO> create(@RequestBody ProcedureStepDTO.RequestDTO request) {
        ProcedureStep step = procedureStepMapper.toEntity(request);

        // Resolver relación con Procedure
        Procedure procedure = procedureRepository.findById(request.getProcedureId())
                .orElseThrow(() -> new RuntimeException("Procedure not found"));
        step.setProcedure(procedure);

        ProcedureStep created = procedureStepService.create(step);
        return ResponseEntity.ok(procedureStepMapper.toResponse(created));
    }

    // 🔹 PUT update step
    @PutMapping("/{id}")
    public ResponseEntity<ProcedureStepDTO.ResponseDTO> update(
            @PathVariable Long id,
            @RequestBody ProcedureStepDTO.RequestDTO request
    ) {
        ProcedureStep step = procedureStepMapper.toEntity(request);
        ProcedureStep updated = procedureStepService.update(id, step);
        return ResponseEntity.ok(procedureStepMapper.toResponse(updated));
    }

    // 🔹 DELETE step
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        procedureStepService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
