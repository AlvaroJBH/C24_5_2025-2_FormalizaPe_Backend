package com.tecsup.formalizape.formalization.controller;

import com.tecsup.formalizape.formalization.dto.StepProgressDTO;
import com.tecsup.formalizape.formalization.mapper.StepProgressMapper;
import com.tecsup.formalizape.formalization.model.BusinessProcedure;
import com.tecsup.formalizape.formalization.model.ProcedureStep;
import com.tecsup.formalizape.formalization.model.StepProgress;
import com.tecsup.formalizape.formalization.repository.BusinessProcedureRepository;
import com.tecsup.formalizape.formalization.repository.ProcedureStepRepository;
import com.tecsup.formalizape.formalization.service.StepProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/step-progress")
@RequiredArgsConstructor
public class StepProgressController {

    private final StepProgressService stepProgressService;
    private final StepProgressMapper stepProgressMapper;
    private final BusinessProcedureRepository businessProcedureRepository;
    private final ProcedureStepRepository procedureStepRepository;

    // 🔹 GET all step progresses
    @GetMapping
    public ResponseEntity<List<StepProgressDTO.ResponseDTO>> getAll() {
        List<StepProgress> progresses = stepProgressService.getAll();
        List<StepProgressDTO.ResponseDTO> response = progresses.stream()
                .map(stepProgressMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔹 GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<StepProgressDTO.ResponseDTO> getById(@PathVariable Long id) {
        StepProgress progress = stepProgressService.getById(id);
        return ResponseEntity.ok(stepProgressMapper.toResponse(progress));
    }

    // 🔹 GET by BusinessProcedure ID
    @GetMapping("/business-procedure/{bpId}")
    public ResponseEntity<List<StepProgressDTO.ResponseDTO>> getByBusinessProcedureId(@PathVariable Long bpId) {
        List<StepProgress> progresses = stepProgressService.getByBusinessProcedureId(bpId);
        List<StepProgressDTO.ResponseDTO> response = progresses.stream()
                .map(stepProgressMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔹 POST create StepProgress
    @PostMapping
    public ResponseEntity<StepProgressDTO.ResponseDTO> create(@RequestBody StepProgressDTO.RequestDTO request) {
        StepProgress progress = stepProgressMapper.toEntity(request);

        // Relaciones
        BusinessProcedure businessProcedure = businessProcedureRepository.findById(request.getBusinessProcedureId())
                .orElseThrow(() -> new RuntimeException("BusinessProcedure not found"));
        ProcedureStep procedureStep = procedureStepRepository.findById(request.getProcedureStepId())
                .orElseThrow(() -> new RuntimeException("ProcedureStep not found"));

        progress.setBusinessProcedure(businessProcedure);
        progress.setProcedureStep(procedureStep);

        StepProgress created = stepProgressService.create(progress);
        return ResponseEntity.ok(stepProgressMapper.toResponse(created));
    }

    // 🔹 PUT update StepProgress
    @PutMapping("/{id}")
    public ResponseEntity<StepProgressDTO.ResponseDTO> update(
            @PathVariable Long id,
            @RequestBody StepProgressDTO.RequestDTO request
    ) {
        StepProgress progress = stepProgressMapper.toEntity(request);
        StepProgress updated = stepProgressService.update(id, progress);
        return ResponseEntity.ok(stepProgressMapper.toResponse(updated));
    }

    // 🔹 DELETE StepProgress
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stepProgressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
