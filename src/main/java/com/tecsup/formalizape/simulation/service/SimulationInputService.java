package com.tecsup.formalizape.simulation.service;

import com.tecsup.formalizape.simulation.model.SimulationInput;
import com.tecsup.formalizape.simulation.repository.SimulationInputRepository;
import com.tecsup.formalizape.formalization.model.Business;
import com.tecsup.formalizape.formalization.repository.BusinessRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimulationInputService {

    private final SimulationInputRepository simulationInputRepository;
    private final BusinessRepository businessRepository;
    private final SimulationCalculationService calculationService;

    /**
     * Crea un nuevo set de datos (versión) y ejecuta la simulación.
     */
    @Transactional
    public SimulationInput createSimulationInput(SimulationInput input) {

        Long businessId = input.getBusiness().getId();

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));

        // Aseguramos la relación real
        input.setBusiness(business);

        // Calcular versión siguiente
        int nextVersion = simulationInputRepository
                .findTopByBusinessIdOrderByVersionNumberDesc(businessId)
                .map(SimulationInput::getVersionNumber)
                .orElse(0) + 1;

        input.setVersionNumber(nextVersion);

        // Guardar input
        input = simulationInputRepository.save(input);

        // Ejecutar cálculos y generar resultados
        calculationService.runSimulation(input);

        return input;
    }

    /**
     * Obtener un input por su id
     */
    @Transactional(readOnly = true)
    public SimulationInput getInput(Long id) {
        return simulationInputRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SimulationInput not found"));
    }

    @Transactional(readOnly = true)
    public List<SimulationInput> getInputsByBusiness(Long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));
        return simulationInputRepository.findAllByBusiness(business);
    }

}
