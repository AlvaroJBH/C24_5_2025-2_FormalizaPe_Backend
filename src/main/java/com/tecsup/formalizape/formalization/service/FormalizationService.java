package com.tecsup.formalizape.formalization.service;

import com.tecsup.formalizape.common.enums.ProgressStatus;
import com.tecsup.formalizape.formalization.dto.FormalizationProcedureDTO;
import com.tecsup.formalizape.formalization.dto.FormalizationStatusDTO;
import com.tecsup.formalizape.formalization.dto.FormalizationStepDTO;
import com.tecsup.formalizape.formalization.dto.FormalizationToggleStepRequest;
import com.tecsup.formalizape.formalization.model.*;
import com.tecsup.formalizape.formalization.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormalizationService {

    private final BusinessRepository businessRepo;
    private final ProcedureRepository procedureRepo;
    private final BusinessProcedureRepository businessProcedureRepo;
    private final ProcedureStepRepository stepRepo;
    private final StepProgressRepository stepProgressRepo;

    // ===========================================================
    // Obtener progreso completo de un business
    // ===========================================================
    public FormalizationStatusDTO getFormalizationStatus(Long businessId) {

        Business business = businessRepo.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        List<Procedure> procedures = procedureRepo.findAll();

        List<FormalizationProcedureDTO> procedureDtos = procedures.stream().map(procedure -> {

            BusinessProcedure bp = businessProcedureRepo.findByBusinessIdAndProcedureId(
                    businessId,
                    procedure.getId()
            ).orElse(null);

            List<ProcedureStep> steps = stepRepo.findByProcedureId(procedure.getId());

            List<FormalizationStepDTO> stepDtos = steps.stream().map(step -> {

                StepProgress sp = (bp != null)
                        ? stepProgressRepo.findByBusinessProcedureIdAndProcedureStepId(
                        bp.getId(), step.getId()
                ).orElse(null)
                        : null;

                return FormalizationStepDTO.builder()
                        .stepId(step.getId())
                        .title(step.getTitle())
                        .description(step.getDescription())
                        .stepOrder(step.getStepOrder())
                        .status(sp != null ? sp.getStatus().name() : "PENDING")
                        .notes(sp != null ? sp.getNotes() : null)
                        .build();

            }).toList();

            int totalSteps = steps.size();

            long completedSteps = stepDtos.stream()
                    .filter(s -> s.getStatus().equals("COMPLETED"))
                    .count();

            double progressPercent = totalSteps == 0
                    ? 0
                    : (completedSteps * 100.0) / totalSteps;

            return FormalizationProcedureDTO.builder()
                    .procedureId(procedure.getId())
                    .name(procedure.getName())
                    .description(procedure.getDescription())
                    .category(procedure.getCategory())
                    .status(bp != null ? bp.getStatus().name() : "PENDING")
                    .steps(stepDtos)
                    .totalSteps(totalSteps)
                    .completedSteps((int) completedSteps)
                    .progressPercent(progressPercent)
                    .build();

        }).toList();

        return FormalizationStatusDTO.builder()
                .businessId(business.getId())
                .businessName(business.getName())
                .procedures(procedureDtos)
                .build();
    }

    // ===========================================================
    // Marcar o desmarcar un paso
    // ===========================================================
    public FormalizationStepDTO toggleStep(FormalizationToggleStepRequest request) {

        Business business = businessRepo.findById(request.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Procedure procedure = procedureRepo.findById(request.getProcedureId())
                .orElseThrow(() -> new RuntimeException("Procedure not found"));

        ProcedureStep step = stepRepo.findById(request.getStepId())
                .orElseThrow(() -> new RuntimeException("Step not found"));

        BusinessProcedure bp = businessProcedureRepo
                .findByBusinessIdAndProcedureId(business.getId(), procedure.getId())
                .orElseGet(() -> {
                    BusinessProcedure newBp = BusinessProcedure.builder()
                            .business(business)
                            .procedure(procedure)
                            .status(ProgressStatus.IN_PROGRESS)
                            .build();
                    return businessProcedureRepo.save(newBp);
                });

        StepProgress sp = stepProgressRepo
                .findByBusinessProcedureIdAndProcedureStepId(bp.getId(), step.getId())
                .orElse(null);

        if (request.isCompleted()) {

            if (sp == null) {
                sp = StepProgress.builder()
                        .businessProcedure(bp)
                        .procedureStep(step)
                        .status(ProgressStatus.COMPLETED)
                        .notes(request.getNotes())
                        .build();
            } else {
                sp.setStatus(ProgressStatus.COMPLETED);
                sp.setNotes(request.getNotes());
            }

        } else {

            if (sp != null) {
                sp.setStatus(ProgressStatus.PENDING);
                sp.setNotes(null);
            }

        }

        sp = stepProgressRepo.save(sp);
        updateBusinessProcedureStatus(bp);

        return FormalizationStepDTO.builder()
                .stepId(step.getId())
                .title(step.getTitle())
                .description(step.getDescription())
                .stepOrder(step.getStepOrder())
                .status(sp.getStatus().name())
                .notes(sp.getNotes())
                .build();
    }

    private void updateBusinessProcedureStatus(BusinessProcedure bp) {

        // 1. Obtener pasos estáticos (ProcedureStep)
        List<ProcedureStep> staticSteps =
                stepRepo.findByProcedureId(bp.getProcedure().getId());

        int totalSteps = staticSteps.size();

        // 2. Obtener los StepProgress existentes
        List<StepProgress> progresses =
                stepProgressRepo.findAll().stream()
                        .filter(sp -> sp.getBusinessProcedure().getId().equals(bp.getId()))
                        .toList();

        // 3. Calcular estado
        if (progresses.isEmpty()) {
            bp.setStatus(ProgressStatus.PENDING);
        } else {
            long completedCount = progresses.stream()
                    .filter(sp -> sp.getStatus() == ProgressStatus.COMPLETED)
                    .count();

            if (completedCount == 0) {
                bp.setStatus(ProgressStatus.PENDING);
            } else if (completedCount < totalSteps) {
                bp.setStatus(ProgressStatus.IN_PROGRESS);
            } else {
                bp.setStatus(ProgressStatus.COMPLETED);
            }
        }

        businessProcedureRepo.save(bp);
    }
}

