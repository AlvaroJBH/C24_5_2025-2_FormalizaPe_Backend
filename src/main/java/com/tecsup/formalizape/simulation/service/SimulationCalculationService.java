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

    @Transactional
    public void runSimulation(SimulationInput input) {

        var regimes = taxRegimeRepository.findAll();

        var results = regimes.stream()
                .map(regime -> calculateForRegime(input, regime))
                .toList();

        simulationResultRepository.saveAll(results);

        markRecommended(results);
    }

    /**
     * Calcula valores + disponibilidad por régimen.
     */
    private SimulationResult calculateForRegime(SimulationInput input, TaxRegime regime) {

        BigDecimal income = input.getMonthlyIncome();
        BigDecimal expense = input.getMonthlyExpense();
        BigDecimal utility = income.subtract(expense);

        BigDecimal monthlyTax = BigDecimal.ZERO;
        BigDecimal monthlyIgv = BigDecimal.ZERO;
        boolean available = true;

        switch (regime.getCode().toUpperCase()) {

            case "RUS": {
                available = income.compareTo(BigDecimal.valueOf(5000)) <= 0
                        && expense.compareTo(BigDecimal.valueOf(5000)) <= 0
                        && input.getQuantity() <= 1;

                if (available) {
                    monthlyTax = BigDecimal.valueOf(20);
                    monthlyIgv  = BigDecimal.ZERO;
                }
                break;
            }

            case "RER": {
                available = income.multiply(BigDecimal.valueOf(12)).compareTo(BigDecimal.valueOf(525_000)) <= 0
                        && input.getQuantity() <= 10;

                if (available) {
                    monthlyTax = income.multiply(BigDecimal.valueOf(0.015));
                    monthlyIgv  = income.multiply(BigDecimal.valueOf(0.18));
                }
                break;
            }

            case "MYPE": {
                available = income.multiply(BigDecimal.valueOf(12)).compareTo(BigDecimal.valueOf(1_700_000)) <= 0
                        && input.getAssets().compareTo(BigDecimal.valueOf(1_000_000)) <= 0;

                if (available) {
                    BigDecimal positiveUtility = utility.max(BigDecimal.ZERO);
                    monthlyTax = positiveUtility.multiply(BigDecimal.valueOf(0.10));
                    monthlyIgv  = income.multiply(BigDecimal.valueOf(0.18));
                }
                break;
            }

            case "GENERAL": {
                available = true;
                BigDecimal positiveUtility = utility.max(BigDecimal.ZERO);
                monthlyTax = positiveUtility.multiply(BigDecimal.valueOf(0.295));
                monthlyIgv  = income.multiply(BigDecimal.valueOf(0.18));
                break;
            }

            default:
                available = false;
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
                .available(available)
                .detailsJson("{\"available\":" + available + "}")
                .build();
    }

    /**
     * Marca como recomendado el régimen disponible de menor costo anual.
     */
    private void markRecommended(List<SimulationResult> results) {

        var availableOnly = results.stream()
                .filter(r -> r.getDetailsJson().contains("\"available\":true"))
                .toList();

        var source = availableOnly.isEmpty() ? results : availableOnly;

        var best = source.stream()
                .min(Comparator.comparing(SimulationResult::getTotalAnnual))
                .orElse(null);

        results.forEach(r -> r.setRecommended(false));
        if (best != null) best.setRecommended(true);
    }
}
