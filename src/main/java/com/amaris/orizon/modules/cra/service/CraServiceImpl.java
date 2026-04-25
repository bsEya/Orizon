package com.amaris.orizon.modules.cra.service;

import com.amaris.orizon.modules.config.entity.FieldDefinition;
import com.amaris.orizon.modules.config.service.FieldConfigService;
import com.amaris.orizon.modules.cra.entity.CraEntry;
import com.amaris.orizon.modules.cra.exception.ResourceNotFoundException;
import com.amaris.orizon.modules.cra.exception.ValidationException;
import com.amaris.orizon.modules.cra.repository.CraEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
//@RequiredArgsConstructor
public class CraServiceImpl implements CraService {

    private final CraEntryRepository craEntryRepository;
    private final FieldConfigService fieldConfigService;

    private static final String CRA_MODULE = "CRA";

    public CraServiceImpl(CraEntryRepository craEntryRepository, @Lazy FieldConfigService fieldConfigService) {
        this.craEntryRepository = craEntryRepository;
        this.fieldConfigService = fieldConfigService;
    }


    @Override
    @Transactional
    public CraEntry saveImputation(CraEntry entry, String teamCode) {
        List<FieldDefinition> configFields = fieldConfigService.getFieldsConfiguration(CRA_MODULE, teamCode);
        Long userId = (entry.getUser() != null) ? entry.getUser().getId() : null;
        validateImputation(entry, userId, configFields, false, null);

        return craEntryRepository.save(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public CraEntry getImputationById(Long id) {
        return craEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imputation not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CraEntry> getUserHistory(Long userId) {
        return craEntryRepository.findByUserIdOrderByDateDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CraEntry> getUserHistoryForPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date must be before end date.");
        }
        return craEntryRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
    }

    @Override
    @Transactional
    public CraEntry updateImputation(Long id, CraEntry entryDetails, String teamCode) {
        CraEntry existingEntry = getImputationById(id);

        List<FieldDefinition> configFields = fieldConfigService.getFieldsConfiguration(CRA_MODULE, teamCode);
        
        // Valider la logique métier avant de mettre à jour
        Long userId = (existingEntry.getUser() != null) ? existingEntry.getUser().getId() : null;
        validateImputation(entryDetails, userId, configFields, true, id);

        // Mettre à jour les champs de l'entité existante
        existingEntry.setDate(entryDetails.getDate());
        existingEntry.setDuration(entryDetails.getDuration());
        existingEntry.setActivityType(entryDetails.getActivityType());
        existingEntry.setComment(entryDetails.getComment());
        existingEntry.setDynamicFields(entryDetails.getDynamicFields());

        return craEntryRepository.save(existingEntry);
    }

    @Override
    @Transactional
    public void deleteImputation(Long id) {
        // getImputationById gère déjà le cas où l'entité n'est pas trouvée en lançant ResourceNotFoundException
        CraEntry entryToDelete = this.getImputationById(id);
        craEntryRepository.delete(entryToDelete);
    }

    /**
     * Méthode privée pour centraliser la logique de validation.
     */
    private void validateImputation(CraEntry entry, Long userId, List<FieldDefinition> configFields, boolean isUpdate, Long entryId) {
        Objects.requireNonNull(userId, "User ID cannot be null.");
        Objects.requireNonNull(entry.getDate(), "Date cannot be null.");
        Objects.requireNonNull(entry.getDuration(), "Duration cannot be null.");

        if (entry.getDuration() <= 0 || entry.getDuration() > 1.0) {
            throw new ValidationException("Duration must be between 0.0 and 1.0.");
        }
        // 1. Validation de la durée totale journalière
        Double totalDurationForDay;
        if (isUpdate) {
            // Pour une mise à jour, on exclut la valeur actuelle de l'entrée en cours de modification
            totalDurationForDay = craEntryRepository.findTotalDurationByUserIdAndDateExcludingEntry(userId, entry.getDate(), entryId);
        } else {
            // Pour une création, on prend tout
            totalDurationForDay = craEntryRepository.findTotalDurationByUserIdAndDate(userId, entry.getDate());
        }

        if (totalDurationForDay + entry.getDuration() > 1.0) {
            throw new ValidationException("Total duration for date " + entry.getDate() + " cannot exceed 1.0.");
        }

        // 2. Validation des champs dynamiques basées sur la configuration du module/équipe
        if (configFields != null) {
            for (FieldDefinition field : configFields) {
                if (field.isRequired()) {
                    String fieldKey = field.getFieldKey();
                    Object value = (entry.getDynamicFields() != null) ? entry.getDynamicFields().get(fieldKey) : null;
                    
                    if (value == null || value.toString().isBlank()) {
                        throw new ValidationException("Required field '" + field.getFieldLabel() + "' is missing or empty.");
                    }
                }

                // Validation par Regex si définie
                if (field.getValidationRegex() != null && !field.getValidationRegex().isBlank()) {
                    String fieldKey = field.getFieldKey();
                    Object value = (entry.getDynamicFields() != null) ? entry.getDynamicFields().get(fieldKey) : null;
                    
                    if (value != null && !value.toString().isBlank() && !value.toString().matches(field.getValidationRegex())) {
                        throw new ValidationException("Field '" + field.getFieldLabel() + "' does not match the required format.");
                    }
                }
            }
        }
    }
}