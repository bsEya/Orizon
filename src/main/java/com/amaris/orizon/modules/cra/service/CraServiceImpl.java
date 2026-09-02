package com.amaris.orizon.modules.cra.service;

import com.amaris.orizon.modules.cra.dto.*;
import com.amaris.orizon.modules.cra.entity.CraEntry;
import com.amaris.orizon.modules.cra.exception.ResourceNotFoundException;
import com.amaris.orizon.modules.cra.exception.ValidationException;
import com.amaris.orizon.modules.cra.repository.CraEntryRepository;
import com.amaris.orizon.modules.referentiel.entity.LP;
import com.amaris.orizon.modules.referentiel.entity.Perimetre;
import com.amaris.orizon.modules.referentiel.entity.Phase;
import com.amaris.orizon.modules.referentiel.entity.Project;
import com.amaris.orizon.modules.referentiel.entity.Squad;
import com.amaris.orizon.modules.referentiel.repository.LPRepository;
import com.amaris.orizon.modules.referentiel.repository.PerimetreRepository;
import com.amaris.orizon.modules.referentiel.repository.PhaseRepository;
import com.amaris.orizon.modules.referentiel.repository.ProjectRepository;
import com.amaris.orizon.modules.referentiel.repository.SquadRepository;
import com.amaris.orizon.modules.cra.entity.ActivityType;
import com.amaris.orizon.modules.cra.repository.ActivityTypeRepository;
import com.amaris.orizon.modules.referentiel.dto.ReferentielItemDto;
import com.amaris.orizon.modules.user.entity.User;
import com.amaris.orizon.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CraServiceImpl implements CraService {

    private static final double MAX_HOURS_PER_DAY = 8.0;

    private final CraEntryRepository craEntryRepository;
    private final UserRepository userRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final ProjectRepository projectRepository;
    private final SquadRepository squadRepository;
    private final PerimetreRepository perimetreRepository;
    private final PhaseRepository phaseRepository;
    private final LPRepository lpRepository;

    // =========================================================
    // JOURNÉE COMPLÈTE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public DayResponse getDayActivities(Long userId, LocalDate date) {
        List<CraEntry> entries = craEntryRepository.findByUserUserIdAndDateOrderByCreatedAtAsc(userId, date);
        List<CraEntryResponse> responses = entries.stream().map(this::toResponse).collect(Collectors.toList());
        double total = responses.stream().mapToDouble(CraEntryResponse::getHours).sum();
        return DayResponse.builder()
                .date(date)
                .userId(userId)
                .activities(responses)
                .totalHours(total)
                .remainingHours(Math.max(0, MAX_HOURS_PER_DAY - total))
                .build();
    }

    @Override
    @Transactional
    public DayResponse saveDay(SaveDayRequest request) {
        validateSaveDayRequest(request);

        // Supprimer les activités existantes de cette journée
        craEntryRepository.deleteByUserUserIdAndDate(request.getUserId(), request.getDate());

        // Valider le total des heures avant insertion
        double totalHours = request.getActivities().stream()
                .mapToDouble(CraEntryRequest::getHours)
                .sum();
        if (totalHours > MAX_HOURS_PER_DAY) {
            throw new ValidationException("Le total des heures (" + totalHours + "h) dépasse le maximum autorisé de " + MAX_HOURS_PER_DAY + "h par jour.");
        }

        User user = findUserById(request.getUserId());

        List<CraEntry> saved = request.getActivities().stream()
                .map(actReq -> {
                    validateActivityRequest(actReq);
                    CraEntry entry = buildEntryFromRequest(actReq, user);
                    entry.setDate(request.getDate());
                    return craEntryRepository.save(entry);
                })
                .collect(Collectors.toList());

        List<CraEntryResponse> responses = saved.stream().map(this::toResponse).collect(Collectors.toList());
        return DayResponse.builder()
                .date(request.getDate())
                .userId(request.getUserId())
                .activities(responses)
                .totalHours(totalHours)
                .remainingHours(Math.max(0, MAX_HOURS_PER_DAY - totalHours))
                .build();
    }

    @Override
    @Transactional
    public void clearDay(Long userId, LocalDate date) {
        craEntryRepository.deleteByUserUserIdAndDate(userId, date);
    }

    // =========================================================
    // ACTIVITÉ INDIVIDUELLE
    // =========================================================

    @Override
    @Transactional
    public CraEntryResponse createActivity(CraEntryRequest request) {
        validateActivityRequest(request);
        validateDailyHoursLimit(request.getUserId(), request.getDate(), request.getHours(), null);

        User user = findUserById(request.getUserId());
        CraEntry entry = buildEntryFromRequest(request, user);
        return toResponse(craEntryRepository.save(entry));
    }

    @Override
    @Transactional(readOnly = true)
    public CraEntryResponse getActivityById(Long id) {
        return toResponse(findEntryById(id));
    }

    @Override
    @Transactional
    public CraEntryResponse updateActivity(Long id, CraEntryRequest request) {
        CraEntry existing = findEntryById(id);
        validateActivityRequest(request);
        validateDailyHoursLimit(existing.getUser().getUserId(), existing.getDate(), request.getHours(), id);

        existing.setHours(request.getHours());
        existing.setActivityType(findActivityTypeById(request.getActivityTypeId()));
        existing.setProject(request.getProjectId() != null ? findProjectById(request.getProjectId()) : null);
        existing.setSquad(request.getSquadId() != null ? findSquadById(request.getSquadId()) : null);
        existing.setPerimetre(request.getPerimetreId() != null ? findPerimetreById(request.getPerimetreId()) : null);
        existing.setPhase(request.getPhaseId() != null ? findPhaseById(request.getPhaseId()) : null);
        existing.setLp(request.getLpId() != null ? findLpById(request.getLpId()) : null);
        existing.setDescription(request.getDescription());

        return toResponse(craEntryRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteActivity(Long id) {
        CraEntry entry = findEntryById(id);
        craEntryRepository.delete(entry);
    }

    @Override
    @Transactional
    public CraEntryResponse duplicateActivity(Long id) {
        CraEntry original = findEntryById(id);

        // Vérifier qu'on peut encore ajouter des heures ce jour-là
        validateDailyHoursLimit(original.getUser().getUserId(), original.getDate(), original.getHours(), null);

        CraEntry duplicate = CraEntry.builder()
                .date(original.getDate())
                .hours(original.getHours())
                .user(original.getUser())
                .activityType(original.getActivityType())
                .project(original.getProject())
                .squad(original.getSquad())
                .perimetre(original.getPerimetre())
                .phase(original.getPhase())
                .lp(original.getLp())
                .description(original.getDescription())
                .build();

        return toResponse(craEntryRepository.save(duplicate));
    }

    // =========================================================
    // HISTORIQUE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<CraEntryResponse> getUserHistory(Long userId) {
        return craEntryRepository.findByUserUserIdOrderByDateDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CraEntryResponse> getUserHistoryForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("La date de début doit être antérieure à la date de fin.");
        }
        return craEntryRepository.findByUserUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // =========================================================
    // MÉTHODES PRIVÉES — Validation
    // =========================================================

    private void validateSaveDayRequest(SaveDayRequest request) {
        if (request.getUserId() == null) throw new ValidationException("L'identifiant utilisateur est obligatoire.");
        if (request.getDate() == null) throw new ValidationException("La date est obligatoire.");
        if (request.getActivities() == null || request.getActivities().isEmpty()) {
            throw new ValidationException("La liste des activités ne peut pas être vide.");
        }
    }

    private void validateActivityRequest(CraEntryRequest request) {
        if (request.getHours() == null || request.getHours() <= 0 || request.getHours() > MAX_HOURS_PER_DAY) {
            throw new ValidationException("La durée doit être comprise entre 0 et " + MAX_HOURS_PER_DAY + " heures.");
        }
        if (request.getActivityTypeId() == null) {
            throw new ValidationException("Le type d'activité est obligatoire.");
        }
    }

    private void validateDailyHoursLimit(Long userId, LocalDate date, Double newHours, Long excludeEntryId) {
        Double existingHours = (excludeEntryId != null)
                ? craEntryRepository.findTotalHoursByUserIdAndDateExcludingEntry(userId, date, excludeEntryId)
                : craEntryRepository.findTotalHoursByUserIdAndDate(userId, date);

        if (existingHours + newHours > MAX_HOURS_PER_DAY) {
            throw new ValidationException(
                    "Total des heures dépasserait " + MAX_HOURS_PER_DAY + "h pour le " + date +
                    " (déjà imputé : " + existingHours + "h, demandé : " + newHours + "h)."
            );
        }
    }

    // =========================================================
    // MÉTHODES PRIVÉES — Lookup
    // =========================================================

    private CraEntry findEntryById(Long id) {
        return craEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activité introuvable avec l'id : " + id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id : " + id));
    }

    private ActivityType findActivityTypeById(Long id) {
        return activityTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Type d'activité introuvable avec l'id : " + id));
    }

    private Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec l'id : " + id));
    }

    private Squad findSquadById(Long id) {
        return squadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Squad introuvable avec l'id : " + id));
    }

    private Perimetre findPerimetreById(Long id) {
        return perimetreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Périmètre introuvable avec l'id : " + id));
    }

    private Phase findPhaseById(Long id) {
        return phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + id));
    }

    private LP findLpById(Long id) {
        return lpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LP introuvable avec l'id : " + id));
    }

    // =========================================================
    // MÉTHODES PRIVÉES — Mapping
    // =========================================================

    private CraEntry buildEntryFromRequest(CraEntryRequest request, User user) {
        return CraEntry.builder()
                .date(request.getDate())
                .hours(request.getHours())
                .user(user)
                .activityType(findActivityTypeById(request.getActivityTypeId()))
                .project(request.getProjectId() != null ? findProjectById(request.getProjectId()) : null)
                .squad(request.getSquadId() != null ? findSquadById(request.getSquadId()) : null)
                .perimetre(request.getPerimetreId() != null ? findPerimetreById(request.getPerimetreId()) : null)
                .phase(request.getPhaseId() != null ? findPhaseById(request.getPhaseId()) : null)
                .lp(request.getLpId() != null ? findLpById(request.getLpId()) : null)
                .description(request.getDescription())
                .build();
    }

    private CraEntryResponse toResponse(CraEntry e) {
        return CraEntryResponse.builder()
                .id(e.getId())
                .date(e.getDate())
                .hours(e.getHours())
                .userId(e.getUser().getUserId())
                .userName(e.getUser().getFirstName() + " " + e.getUser().getLastName())
                .activityType(e.getActivityType() != null ? toRefDto(e.getActivityType().getId(), e.getActivityType().getCode(), e.getActivityType().getLabel()) : null)
                .project(e.getProject() != null ? toRefDto(e.getProject().getId(), e.getProject().getCode(), e.getProject().getLabel()) : null)
                .squad(e.getSquad() != null ? toRefDto(e.getSquad().getId(), e.getSquad().getCode(), e.getSquad().getLabel()) : null)
                .perimetre(e.getPerimetre() != null ? toRefDto(e.getPerimetre().getId(), e.getPerimetre().getCode(), e.getPerimetre().getLabel()) : null)
                .phase(e.getPhase() != null ? toRefDto(e.getPhase().getId(), e.getPhase().getCode(), e.getPhase().getLabel()) : null)
                .lp(e.getLp() != null ? toRefDto(e.getLp().getId(), e.getLp().getCode(), e.getLp().getLabel()) : null)
                .description(e.getDescription())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private ReferentielItemDto toRefDto(Long id, String code, String label) {
        return ReferentielItemDto.builder().id(id).code(code).label(label).active(true).build();
    }
}