package com.tecsup.formalizape.simulation.controller;

import com.tecsup.formalizape.formalization.model.Business;
import com.tecsup.formalizape.formalization.repository.BusinessRepository;
import com.tecsup.formalizape.simulation.dto.SimulationInputRequest;
import com.tecsup.formalizape.simulation.dto.SimulationInputResponse;
import com.tecsup.formalizape.simulation.mapper.SimulationInputMapper;
import com.tecsup.formalizape.simulation.model.SimulationInput;
import com.tecsup.formalizape.simulation.service.SimulationInputService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulations/inputs")
@RequiredArgsConstructor
public class SimulationInputController {

    private final SimulationInputService simulationInputService;
    private final BusinessRepository businessRepository;
    private final SimulationInputMapper inputMapper;

    /**
     * Crear un nuevo input de simulación y ejecutar cálculo.
     */
    @PostMapping
    public ResponseEntity<SimulationInputResponse> createInput(
            @RequestBody SimulationInputRequest request
    ) {
        // Convertir DTO → Entity (sin relaciones)
        SimulationInput input = inputMapper.toEntity(request);

        // 🔹 Resolver la relación con Business usando el ID del DTO
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));
        input.setBusiness(business);

        // Llamar servicio (ya recibe la entidad completa)
        SimulationInput saved = simulationInputService.createSimulationInput(input);

        // Convertir Entity → ResponseDTO
        SimulationInputResponse response = inputMapper.toResponse(saved);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * Obtener un input por ID
     */
    @GetMapping("/{inputId}")
    public ResponseEntity<SimulationInputResponse> getInput(@PathVariable Long inputId) {
        SimulationInput input = simulationInputService.getInput(inputId);

        SimulationInputResponse response = inputMapper.toResponse(input);

        return ResponseEntity.ok(response);
    }

    /**
     * Obtener todos los inputs de un business
     */
    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<SimulationInputResponse>> getInputsByBusiness(@PathVariable Long businessId) {
        List<SimulationInput> inputs = simulationInputService.getInputsByBusiness(businessId);
        List<SimulationInputResponse> responses = inputs.stream()
                .map(inputMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

}
