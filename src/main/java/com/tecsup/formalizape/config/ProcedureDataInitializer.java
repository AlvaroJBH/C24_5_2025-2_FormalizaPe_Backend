package com.tecsup.formalizape.init;

import com.tecsup.formalizape.formalization.model.Procedure;
import com.tecsup.formalizape.formalization.model.ProcedureStep;
import com.tecsup.formalizape.formalization.repository.ProcedureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProcedureDataInitializer implements CommandLineRunner {

    private final ProcedureRepository procedureRepository;

    @Override
    public void run(String... args) {
        createConstitucionEmpresa();
        createRegistroRUC();
        createLicenciaMunicipal();
        createRegistroEssalud();
    }

    private void createConstitucionEmpresa() {
        if (procedureRepository.existsByName("Constitución de Empresa (SUNARP)")) return;

        Procedure procedure = Procedure.builder()
                .name("Constitución de Empresa (SUNARP)")
                .description("Registro de constitución de sociedad en los Registros Públicos")
                .category("Legal")
                .build();

        Set<ProcedureStep> steps = new LinkedHashSet<>();
        steps.add(ProcedureStep.builder()
                .title("Búsqueda de nombre")
                .description("Verificar disponibilidad del nombre de la empresa")
                .stepOrder(1)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Elaboración de minuta")
                .description("Redacción del estatuto social")
                .stepOrder(2)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Elevación a escritura pública")
                .description("Formalización ante notario público")
                .stepOrder(3)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Inscripción en SUNARP")
                .description("Registro en los Registros Públicos")
                .stepOrder(4)
                .procedure(procedure)
                .build());

        procedure.setSteps(steps);
        procedureRepository.save(procedure);
    }

    private void createRegistroRUC() {
        if (procedureRepository.existsByName("Registro RUC (SUNAT)")) return;

        Procedure procedure = Procedure.builder()
                .name("Registro RUC (SUNAT)")
                .description("Obtención del Registro Único de Contribuyentes")
                .category("Fiscal")
                .build();

        Set<ProcedureStep> steps = new LinkedHashSet<>();
        steps.add(ProcedureStep.builder()
                .title("Preparar documentos")
                .description("Reunir documentación necesaria")
                .stepOrder(1)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Completar formulario")
                .description("Llenar formulario 2119")
                .stepOrder(2)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Presentar solicitud")
                .description("Tramitar en oficina SUNAT o en línea")
                .stepOrder(3)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Recibir RUC")
                .description("Obtener número de RUC y clave SOL")
                .stepOrder(4)
                .procedure(procedure)
                .build());

        procedure.setSteps(steps);
        procedureRepository.save(procedure);
    }

    private void createLicenciaMunicipal() {
        if (procedureRepository.existsByName("Licencia Municipal")) return;

        Procedure procedure = Procedure.builder()
                .name("Licencia Municipal")
                .description("Autorización municipal de funcionamiento")
                .category("Municipal")
                .build();

        Set<ProcedureStep> steps = new LinkedHashSet<>();
        steps.add(ProcedureStep.builder()
                .title("Solicitar certificado de zonificación")
                .description("Verificar compatibilidad de uso")
                .stepOrder(1)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Preparar documentación")
                .description("Reunir requisitos municipales")
                .stepOrder(2)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Presentar solicitud")
                .description("Tramitar en la municipalidad correspondiente")
                .stepOrder(3)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Inspección municipal")
                .description("Verificación del local comercial")
                .stepOrder(4)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Obtener licencia")
                .description("Recibir autorización de funcionamiento")
                .stepOrder(5)
                .procedure(procedure)
                .build());

        procedure.setSteps(steps);
        procedureRepository.save(procedure);
    }

    private void createRegistroEssalud() {
        if (procedureRepository.existsByName("Registro ESSALUD")) return;

        Procedure procedure = Procedure.builder()
                .name("Registro ESSALUD")
                .description("Inscripción de trabajadores en el seguro social")
                .category("Laboral")
                .build();

        Set<ProcedureStep> steps = new LinkedHashSet<>();
        steps.add(ProcedureStep.builder()
                .title("Activar RUC")
                .description("Completar trámites en SUNAT primero")
                .stepOrder(1)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Registrar trabajadores")
                .description("Inscribir empleados en planilla")
                .stepOrder(2)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Tramitar en ESSALUD")
                .description("Presentar documentación")
                .stepOrder(3)
                .procedure(procedure)
                .build());
        steps.add(ProcedureStep.builder()
                .title("Activar cobertura")
                .description("Verificar cobertura médica activa")
                .stepOrder(4)
                .procedure(procedure)
                .build());

        procedure.setSteps(steps);
        procedureRepository.save(procedure);
    }
}
