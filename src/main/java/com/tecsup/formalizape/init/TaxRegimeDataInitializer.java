package com.tecsup.formalizape.simulation.init;

import com.tecsup.formalizape.simulation.model.TaxRegime;
import com.tecsup.formalizape.simulation.repository.TaxRegimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaxRegimeDataInitializer implements CommandLineRunner {

    private final TaxRegimeRepository taxRegimeRepository;

    @Override
    public void run(String... args) {
        createTaxRegime("RUS", "Régimen Único Simplificado", "Régimen para contribuyentes con ingresos pequeños");
        createTaxRegime("RER", "Régimen Especial", "Régimen con tasa fija del 1.5% sobre ingresos y libros simplificados");
        createTaxRegime("MYPE", "MYPE Tributario", "10% sobre utilidad hasta 15 UIT, beneficios para pequeñas empresas");
        createTaxRegime("GENERAL", "Régimen General", "29.5% sobre utilidad neta + IGV, sin limitaciones de ingresos");
    }

    private void createTaxRegime(String code, String name, String description) {
        if (taxRegimeRepository.existsByCode(code)) return;

        TaxRegime regime = TaxRegime.builder()
                .code(code)
                .name(name)
                .description(description)
                .build();

        taxRegimeRepository.save(regime);
    }
}
