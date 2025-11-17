package com.tecsup.formalizape.simulation.controller;

import com.tecsup.formalizape.simulation.dto.SimulationResultResponse;
import com.tecsup.formalizape.simulation.service.SimulationResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulations/results")
@RequiredArgsConstructor
public class SimulationResultController {

    private final SimulationResultService simulationResultService;

    /**
     * Obtener todos los resultados para un input de simulación
     */
    @GetMapping("/{inputId}")
    public ResponseEntity<SimulationResultResponse> getResults(@PathVariable Long inputId) {
        SimulationResultResponse response = simulationResultService.getResultsForInput(inputId);
        return ResponseEntity.ok(response);
    }
}
