package com.tecsup.formalizape.init;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.formalizape.simulation.model.TaxRegime;
import com.tecsup.formalizape.simulation.repository.TaxRegimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TaxRegimeDataInitializer implements CommandLineRunner {

    private final TaxRegimeRepository taxRegimeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args) {
        createTaxRegime(
                "RUS",
                "Régimen Único Simplificado (Nuevo RUS)",
                "Categoría 1 - Cuota fija mensual",
                Map.of(
                        "beneficios", List.of("Sin libros contables", "Sin declaraciones mensuales"),
                        "requisitos", List.of("Ingresos ≤ S/ 5,000", "Compras ≤ S/ 5,000")
                )
        );

        createTaxRegime(
                "RER",
                "Régimen Especial (RER)",
                "1.5% de ingresos + IGV 18%",
                Map.of(
                        "beneficios", List.of("Tasa fija del 1.5%", "Libros simplificados"),
                        "requisitos", List.of("Ingresos ≤ S/ 525,000 anuales", "Máximo 10 trabajadores")
                )
        );

        createTaxRegime(
                "MYPE",
                "MYPE Tributario",
                "10% sobre utilidad (hasta 15 UIT)",
                Map.of(
                        "beneficios", List.of("Tasa reducida para pequeñas empresas", "Depreciación acelerada"),
                        "requisitos", List.of("Ingresos ≤ S/ 1,700,000 anuales", "Activos ≤ S/ 1,000,000")
                )
        );

        createTaxRegime(
                "GENERAL",
                "Régimen General",
                "29.5% sobre utilidad neta + IGV 18%",
                Map.of(
                        "beneficios", List.of("Sin limitaciones de ingresos", "Crédito fiscal completo"),
                        "requisitos", List.of("Sin límite de ingresos", "Libros contables completos")
                )
        );
    }

    private void createTaxRegime(String code, String name, String description, Map<String, Object> rules) {

        if (taxRegimeRepository.existsByCode(code)) return;

        String rulesJson = "";
        try {
            rulesJson = objectMapper.writeValueAsString(rules);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializando JSON de reglas", e);
        }

        TaxRegime regime = TaxRegime.builder()
                .code(code)
                .name(name)
                .description(description)
                .rulesJson(rulesJson)
                .build();

        taxRegimeRepository.save(regime);
    }
}
