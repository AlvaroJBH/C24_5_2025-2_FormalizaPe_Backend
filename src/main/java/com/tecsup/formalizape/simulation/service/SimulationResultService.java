package com.tecsup.formalizape.simulation.service;

import com.tecsup.formalizape.simulation.dto.SimulationResultResponse;
import com.tecsup.formalizape.simulation.mapper.SimulationResultMapper;
import com.tecsup.formalizape.simulation.model.SimulationInput;
import com.tecsup.formalizape.simulation.model.SimulationResult;
import com.tecsup.formalizape.simulation.repository.SimulationResultRepository;
import com.tecsup.formalizape.simulation.repository.SimulationInputRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimulationResultService {

    private final SimulationResultRepository simulationResultRepository;
    private final SimulationInputRepository simulationInputRepository;
    private final SimulationResultMapper resultMapper;

    /**
     * Retorna todos los resultados de una versión de simulación con DTO.
     */
    public SimulationResultResponse getResultsForInput(Long simulationInputId) {

        SimulationInput input = simulationInputRepository.findById(simulationInputId)
                .orElseThrow(() -> new EntityNotFoundException("SimulationInput not found"));

        List<SimulationResult> results = simulationResultRepository.findBySimulationInputId(simulationInputId);

        return buildResponse(input, results);
    }

    private SimulationResultResponse buildResponse(SimulationInput input, List<SimulationResult> results) {
        Long recommendedId = results.stream()
                .filter(SimulationResult::getRecommended)
                .findFirst()
                .map(SimulationResult::getId)
                .orElse(null);

        return SimulationResultResponse.builder()
                .simulationInputId(input.getId())
                .recommendedResultId(recommendedId)
                .results(results.stream().map(resultMapper::toItem).toList())
                .build();
    }
}
