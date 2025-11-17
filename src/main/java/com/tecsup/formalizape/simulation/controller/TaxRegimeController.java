package com.tecsup.formalizape.simulation.controller;

import com.tecsup.formalizape.simulation.dto.TaxRegimeResponse;
import com.tecsup.formalizape.simulation.mapper.TaxRegimeMapper;
import com.tecsup.formalizape.simulation.model.TaxRegime;
import com.tecsup.formalizape.simulation.repository.TaxRegimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulations/tax-regimes")
@RequiredArgsConstructor
public class TaxRegimeController {

    private final TaxRegimeRepository taxRegimeRepository;
    private final TaxRegimeMapper taxRegimeMapper;

    /**
     * Obtener todos los regímenes tributarios
     */
    @GetMapping
    public ResponseEntity<List<TaxRegimeResponse>> getAllRegimes() {
        List<TaxRegime> regimes = taxRegimeRepository.findAll();
        List<TaxRegimeResponse> response = regimes.stream()
                .map(taxRegimeMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
