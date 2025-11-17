package com.tecsup.formalizape.simulation.repository;

import com.tecsup.formalizape.simulation.model.TaxRegime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxRegimeRepository extends JpaRepository<TaxRegime, Long> {

    // Buscar un régimen por código (RUS, RER, MYPE, etc.)
    Optional<TaxRegime> findByCode(String code);

    boolean existsByCode(String code);
    // Regímenes activos
    List<TaxRegime> findAllByActiveTrue();

    // Regímenes activos ordenados alfabéticamente
    List<TaxRegime> findAllByActiveTrueOrderByNameAsc();
}
