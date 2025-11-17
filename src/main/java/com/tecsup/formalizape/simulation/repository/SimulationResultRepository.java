package com.tecsup.formalizape.simulation.repository;

import com.tecsup.formalizape.simulation.model.SimulationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimulationResultRepository extends JpaRepository<SimulationResult, Long> {

    // Traer todos los resultados de un SimulationInput
    List<SimulationResult> findBySimulationInputId(Long simulationInputId);

    // Obtener el resultado recomendado
    Optional<SimulationResult> findBySimulationInputIdAndRecommendedTrue(Long simulationInputId);

    // Eliminar todos los resultados asociados (si se recalcula)
    void deleteBySimulationInputId(Long simulationInputId);

    // Contar resultados asociados
    long countBySimulationInputId(Long simulationInputId);
}
