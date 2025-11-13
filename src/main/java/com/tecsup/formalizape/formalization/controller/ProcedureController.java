package com.tecsup.formalizape.formalization.controller;

import com.tecsup.formalizape.formalization.dto.ProcedureDTO;
import com.tecsup.formalizape.formalization.mapper.ProcedureMapper;
import com.tecsup.formalizape.formalization.model.Procedure;
import com.tecsup.formalizape.formalization.service.ProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedures")
@RequiredArgsConstructor
public class ProcedureController {

    private final ProcedureService procedureService;
    private final ProcedureMapper procedureMapper;

    // 🔹 GET all procedures
    @GetMapping
    public ResponseEntity<List<ProcedureDTO.ResponseDTO>> getAll() {
        List<Procedure> procedures = procedureService.getAll();
        List<ProcedureDTO.ResponseDTO> response = procedures.stream()
                .map(procedureMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔹 GET procedure by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProcedureDTO.ResponseDTO> getById(@PathVariable Long id) {
        Procedure procedure = procedureService.getById(id);
        return ResponseEntity.ok(procedureMapper.toResponse(procedure));
    }

    // 🔹 POST create procedure
    @PostMapping
    public ResponseEntity<ProcedureDTO.ResponseDTO> create(
            @RequestBody ProcedureDTO.RequestDTO request
    ) {
        Procedure procedure = procedureMapper.toEntity(request);
        Procedure created = procedureService.create(procedure);
        return ResponseEntity.ok(procedureMapper.toResponse(created));
    }

    // 🔹 PUT update procedure
    @PutMapping("/{id}")
    public ResponseEntity<ProcedureDTO.ResponseDTO> update(
            @PathVariable Long id,
            @RequestBody ProcedureDTO.RequestDTO request
    ) {
        Procedure procedure = procedureMapper.toEntity(request);
        Procedure updated = procedureService.update(id, procedure);
        return ResponseEntity.ok(procedureMapper.toResponse(updated));
    }

    // 🔹 DELETE procedure
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        procedureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
