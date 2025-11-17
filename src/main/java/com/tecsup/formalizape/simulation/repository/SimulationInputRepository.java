package com.tecsup.formalizape.simulation.repository;

import com.tecsup.formalizape.formalization.model.Business;
import com.tecsup.formalizape.simulation.model.SimulationInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimulationInputRepository extends JpaRepository<SimulationInput, Long> {

    // Todos los inputs para un business (activos)
    List<SimulationInput> findByBusinessIdAndActiveTrueOrderByCreatedAtDesc(Long businessId);

    // Última versión creada para un business
    Optional<SimulationInput> findTopByBusinessIdOrderByVersionNumberDesc(Long businessId);

    // Buscar un input por business + versión específica
    Optional<SimulationInput> findByBusinessIdAndVersionNumber(Long businessId, Integer versionNumber);

    List<SimulationInput> findAllByBusiness(Business business);
}
