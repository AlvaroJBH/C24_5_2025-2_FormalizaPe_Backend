package com.tecsup.formalizape.formalization.controller;

import com.tecsup.formalizape.formalization.dto.BusinessProcedureDTO;
import com.tecsup.formalizape.formalization.mapper.BusinessProcedureMapper;
import com.tecsup.formalizape.formalization.model.Business;
import com.tecsup.formalizape.formalization.model.BusinessProcedure;
import com.tecsup.formalizape.formalization.model.Procedure;
import com.tecsup.formalizape.formalization.repository.BusinessRepository;
import com.tecsup.formalizape.formalization.repository.ProcedureRepository;
import com.tecsup.formalizape.formalization.service.BusinessProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business-procedures")
@RequiredArgsConstructor
public class BusinessProcedureController {

    private final BusinessProcedureService businessProcedureService;
    private final BusinessProcedureMapper businessProcedureMapper;
    private final BusinessRepository businessRepository;
    private final ProcedureRepository procedureRepository;

    // 🔹 GET all
    @GetMapping
    public ResponseEntity<List<BusinessProcedureDTO.ResponseDTO>> getAll() {
        List<BusinessProcedure> list = businessProcedureService.getAll();
        List<BusinessProcedureDTO.ResponseDTO> response = list.stream()
                .map(businessProcedureMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔹 GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<BusinessProcedureDTO.ResponseDTO> getById(@PathVariable Long id) {
        BusinessProcedure bp = businessProcedureService.getById(id);
        return ResponseEntity.ok(businessProcedureMapper.toResponse(bp));
    }

    // 🔹 GET by Business ID
    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<BusinessProcedureDTO.ResponseDTO>> getByBusinessId(@PathVariable Long businessId) {
        List<BusinessProcedure> list = businessProcedureService.getByBusinessId(businessId);
        List<BusinessProcedureDTO.ResponseDTO> response = list.stream()
                .map(businessProcedureMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔹 POST create
    @PostMapping
    public ResponseEntity<BusinessProcedureDTO.ResponseDTO> create(@RequestBody BusinessProcedureDTO.RequestDTO request) {
        BusinessProcedure bp = businessProcedureMapper.toEntity(request);

        // Resolver relaciones
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));
        Procedure procedure = procedureRepository.findById(request.getProcedureId())
                .orElseThrow(() -> new RuntimeException("Procedure not found"));

        bp.setBusiness(business);
        bp.setProcedure(procedure);

        BusinessProcedure created = businessProcedureService.create(bp);
        return ResponseEntity.ok(businessProcedureMapper.toResponse(created));
    }

    // 🔹 PUT update
    @PutMapping("/{id}")
    public ResponseEntity<BusinessProcedureDTO.ResponseDTO> update(
            @PathVariable Long id,
            @RequestBody BusinessProcedureDTO.RequestDTO request
    ) {
        BusinessProcedure bp = businessProcedureMapper.toEntity(request);
        BusinessProcedure updated = businessProcedureService.update(id, bp);
        return ResponseEntity.ok(businessProcedureMapper.toResponse(updated));
    }

    // 🔹 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        businessProcedureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
