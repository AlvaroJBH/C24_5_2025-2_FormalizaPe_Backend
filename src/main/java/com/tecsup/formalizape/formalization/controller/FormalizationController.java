package com.tecsup.formalizape.formalization.controller;

import com.tecsup.formalizape.formalization.dto.FormalizationStatusDTO;
import com.tecsup.formalizape.formalization.dto.FormalizationStepDTO;
import com.tecsup.formalizape.formalization.dto.FormalizationToggleStepRequest;
import com.tecsup.formalizape.formalization.service.FormalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/formalization")
@RequiredArgsConstructor
public class FormalizationController {

    private final FormalizationService formalizationService;

    @GetMapping("/business/{businessId}")
    public ResponseEntity<FormalizationStatusDTO> getFormalizationStatus(
            @PathVariable Long businessId
    ) {
        return ResponseEntity.ok(formalizationService.getFormalizationStatus(businessId));
    }

    @PostMapping("/step/toggle")
    public ResponseEntity<FormalizationStepDTO> toggleStep(
            @RequestBody FormalizationToggleStepRequest request
    ) {
        return ResponseEntity.ok(formalizationService.toggleStep(request));
    }
}

