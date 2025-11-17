package com.tecsup.formalizape.simulation.service;

import com.tecsup.formalizape.simulation.model.SimulationInput;
import com.tecsup.formalizape.simulation.model.SimulationResult;
import com.tecsup.formalizape.simulation.model.TaxRegime;
import com.tecsup.formalizape.simulation.repository.SimulationResultRepository;
import com.tecsup.formalizape.simulation.repository.TaxRegimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimulationCalculationService {

    private final TaxRegimeRepository taxRegimeRepository;
    private final SimulationResultRepository simulationResultRepository;

    /**
     * Corre la simulación completa → genera un SimulationResult por cada régimen y marca el recomendado.
     */
    @Transactional
    public void runSimulation(SimulationInput input) {

        var regimes = taxRegimeRepository.findAll();
        var results = regimes.stream()
                .map(regime -> calculateForRegime(input, regime))
                .toList();

        // Guardar todos los resultados
        simulationResultRepository.saveAll(results);

        // Marcar el recomendado (menor total anual)
        markRecommended(results);
    }

    /**
     * Calcula los valores para un régimen específico según su código.
     */
    private SimulationResult calculateForRegime(SimulationInput input, TaxRegime regime) {

        BigDecimal monthlyTax = BigDecimal.ZERO;
        BigDecimal monthlyIgv  = BigDecimal.ZERO;

        BigDecimal income = input.getMonthlyIncome();
        BigDecimal expense = input.getMonthlyExpense();
        BigDecimal utility = income.subtract(expense);

        switch (regime.getCode().toUpperCase()) {
            case "RUS":
                monthlyTax = BigDecimal.valueOf(20); // cuota fija mensual ejemplo
                monthlyIgv = BigDecimal.ZERO;
                break;

            case "RER":
                monthlyTax = income.multiply(BigDecimal.valueOf(0.015));
                monthlyIgv = income.multiply(BigDecimal.valueOf(0.18));
                break;

            case "MYPE":
                monthlyTax = utility.multiply(BigDecimal.valueOf(0.10));
                monthlyIgv = income.multiply(BigDecimal.valueOf(0.18));
                break;

            case "GENERAL":
                monthlyTax = utility.multiply(BigDecimal.valueOf(0.295));
                monthlyIgv = income.multiply(BigDecimal.valueOf(0.18));
                break;

            default:
                // si aparece un régimen nuevo no definido, dejar todo en cero
                monthlyTax = BigDecimal.ZERO;
                monthlyIgv = BigDecimal.ZERO;
                break;
        }

        BigDecimal totalMonthly = monthlyTax.add(monthlyIgv);
        BigDecimal totalAnnual  = totalMonthly.multiply(BigDecimal.valueOf(12));

        return SimulationResult.builder()
                .simulationInput(input)
                .taxRegime(regime)
                .monthlyTax(monthlyTax)
                .monthlyIgv(monthlyIgv)
                .totalMonthly(totalMonthly)
                .totalAnnual(totalAnnual)
                .recommended(false)
                .build();
    }

    /**
     * Marca como recomendado el resultado con menor total anual.
     */
    private void markRecommended(List<SimulationResult> results) {
        SimulationResult best = results.stream()
                .min(Comparator.comparing(SimulationResult::getTotalAnnual))
                .orElse(null);

        if (best != null) {
            results.forEach(r -> r.setRecommended(false));
            best.setRecommended(true);
        }
    }
}
