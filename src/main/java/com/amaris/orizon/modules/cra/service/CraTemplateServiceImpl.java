package com.amaris.orizon.modules.cra.service;

import com.amaris.orizon.modules.cra.dto.CraTemplateResponse;
import com.amaris.orizon.modules.cra.dto.CraTemplateSaveRequest;
import com.amaris.orizon.modules.cra.entity.CraTemplate;
import com.amaris.orizon.modules.cra.exception.ResourceNotFoundException;
import com.amaris.orizon.modules.cra.repository.ActivityTypeRepository;
import com.amaris.orizon.modules.cra.repository.CraTemplateRepository;
import com.amaris.orizon.modules.referentiel.dto.ReferentielItemDto;
import com.amaris.orizon.modules.referentiel.repository.LPRepository;
import com.amaris.orizon.modules.referentiel.repository.PerimetreRepository;
import com.amaris.orizon.modules.referentiel.repository.PhaseRepository;
import com.amaris.orizon.modules.referentiel.repository.ProjectRepository;
import com.amaris.orizon.modules.referentiel.repository.SquadRepository;
import com.amaris.orizon.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CraTemplateServiceImpl implements CraTemplateService {

    private final CraTemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final ProjectRepository projectRepository;
    private final SquadRepository squadRepository;
    private final PerimetreRepository perimetreRepository;
    private final PhaseRepository phaseRepository;
    private final LPRepository lpRepository;

    @Override
    @Transactional
    public CraTemplateResponse saveTemplate(CraTemplateSaveRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + request.getUserId()));

        CraTemplate template = CraTemplate.builder()
                .label(request.getLabel())
                .user(user)
                .activityType(request.getActivityTypeId() != null
                        ? activityTypeRepository.findById(request.getActivityTypeId()).orElse(null) : null)
                .project(request.getProjectId() != null
                        ? projectRepository.findById(request.getProjectId()).orElse(null) : null)
                .squad(request.getSquadId() != null
                        ? squadRepository.findById(request.getSquadId()).orElse(null) : null)
                .perimetre(request.getPerimetreId() != null
                        ? perimetreRepository.findById(request.getPerimetreId()).orElse(null) : null)
                .phase(request.getPhaseId() != null
                        ? phaseRepository.findById(request.getPhaseId()).orElse(null) : null)
                .lp(request.getLpId() != null
                        ? lpRepository.findById(request.getLpId()).orElse(null) : null)
                .description(request.getDescription())
                .build();

        return toResponse(templateRepository.save(template));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CraTemplateResponse> getUserTemplates(Long userId) {
        return templateRepository.findByUserUserIdOrderByLabelAsc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTemplate(Long id) {
        CraTemplate t = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Modèle introuvable avec l'id : " + id));
        templateRepository.delete(t);
    }

    private CraTemplateResponse toResponse(CraTemplate t) {
        return CraTemplateResponse.builder()
                .id(t.getId())
                .label(t.getLabel())
                .userId(t.getUser().getUserId())
                .activityType(t.getActivityType() != null
                        ? toRefDto(t.getActivityType().getId(), t.getActivityType().getCode(), t.getActivityType().getLabel()) : null)
                .project(t.getProject() != null
                        ? toRefDto(t.getProject().getId(), t.getProject().getCode(), t.getProject().getLabel()) : null)
                .squad(t.getSquad() != null
                        ? toRefDto(t.getSquad().getId(), t.getSquad().getCode(), t.getSquad().getLabel()) : null)
                .perimetre(t.getPerimetre() != null
                        ? toRefDto(t.getPerimetre().getId(), t.getPerimetre().getCode(), t.getPerimetre().getLabel()) : null)
                .phase(t.getPhase() != null
                        ? toRefDto(t.getPhase().getId(), t.getPhase().getCode(), t.getPhase().getLabel()) : null)
                .lp(t.getLp() != null
                        ? toRefDto(t.getLp().getId(), t.getLp().getCode(), t.getLp().getLabel()) : null)
                .description(t.getDescription())
                .createdAt(t.getCreatedAt())
                .build();
    }

    private ReferentielItemDto toRefDto(Long id, String code, String label) {
        return ReferentielItemDto.builder().id(id).code(code).label(label).active(true).build();
    }
}
