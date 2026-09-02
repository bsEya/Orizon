package com.amaris.orizon.config;

import com.amaris.orizon.modules.cra.entity.ActivityType;
import com.amaris.orizon.modules.cra.repository.ActivityTypeRepository;
import com.amaris.orizon.modules.referentiel.entity.*;
import com.amaris.orizon.modules.referentiel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Initialise les données de référence au démarrage si la base est vide.
 * Toutes les listes déroulantes du CRA sont peuplées ici.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ActivityTypeRepository activityTypeRepository;
    private final ProjectRepository projectRepository;
    private final SquadRepository squadRepository;
    private final PerimetreRepository perimetreRepository;
    private final PhaseRepository phaseRepository;
    private final LPRepository lpRepository;

    @Override
    public void run(String... args) {
        initActivityTypes();
        initProjects();
        initSquads();
        initPerimetres();
        initPhases();
        initLPs();
    }

    // ============================================================
    // 1 — TYPES D'ACTIVITÉ (Mission / Travail / Congé / Férié)
    // ============================================================
    private void initActivityTypes() {
        if (activityTypeRepository.count() > 0) return;
        log.info("Initialisation des types d'activité...");
        activityTypeRepository.saveAll(List.of(
            ActivityType.builder().code("MISSION").label("Mission").billable(true).active(true).build(),
            ActivityType.builder().code("TRAVAIL").label("Travail").billable(true).active(true).build(),
            ActivityType.builder().code("CONGE").label("Congé").billable(false).active(true).build(),
            ActivityType.builder().code("FERIE").label("Férié").billable(false).active(true).build()
        ));
        log.info("Types d'activité initialisés.");
    }

    // ============================================================
    // 2 — PROJETS
    // ============================================================
    private void initProjects() {
        if (projectRepository.count() > 0) return;
        log.info("Initialisation des projets...");
        projectRepository.saveAll(List.of(
            Project.builder().code("AUTORISATION").label("Autorisation").build(),
            Project.builder().code("BOOTCAMP").label("Bootcamp").build(),
            Project.builder().code("CAMUNDA_AUTOSENSE").label("Camunda Autosense").build(),
            Project.builder().code("CAMUNDA_PROCESS_DIGIT").label("Camunda Process Digit").build(),
            Project.builder().code("FORMATION").label("Formation").build(),
            Project.builder().code("ORIZON").label("Orizon").build()
        ));
        log.info("Projets initialisés.");
    }

    // ============================================================
    // 3 — SQUADS
    // ============================================================
    private void initSquads() {
        if (squadRepository.count() > 0) return;
        log.info("Initialisation des squads...");
        squadRepository.saveAll(List.of(
            Squad.builder().code("AUTORISATION").label("Autorisation").build(),
            Squad.builder().code("SQUAD1").label("Squad 1").build(),
            Squad.builder().code("SQUAD2").label("Squad 2").build(),
            Squad.builder().code("SQUAD3").label("Squad 3").build(),
            Squad.builder().code("TRANSVERSES").label("Transverses").build()
        ));
        log.info("Squads initialisés.");
    }

    // ============================================================
    // 4 — PÉRIMÈTRES
    // ============================================================
    private void initPerimetres() {
        if (perimetreRepository.count() > 0) return;
        log.info("Initialisation des périmètres...");
        perimetreRepository.saveAll(List.of(
            Perimetre.builder().code("ATELIER_METHODE").label("Atelier méthode commune modélisation").build(),
            Perimetre.builder().code("AUDIT360").label("Audit360").build(),
            Perimetre.builder().code("AUTOCOMPLETE").label("AutoComplete").build(),
            Perimetre.builder().code("AUTORISATION").label("Autorisation").build(),
            Perimetre.builder().code("BOOTCAMP").label("Bootcamp").build(),
            Perimetre.builder().code("CADRAGE_DI").label("Cadrage DI").build(),
            Perimetre.builder().code("CADRAGE_IRU_DOLMEN").label("Cadrage IRU DOLMEN").build(),
            Perimetre.builder().code("CADRAGE_LP68_2").label("Cadrage LP68.2").build(),
            Perimetre.builder().code("CADRAGE_LP68_5").label("Cadrage LP68.5").build(),
            Perimetre.builder().code("CADRAGE_MEETING").label("Cadrage meeting").build(),
            Perimetre.builder().code("CADRAGE_OTOBO").label("Cadrage OTOBO").build(),
            Perimetre.builder().code("CADRAGE_VDR").label("Cadrage VDR").build(),
            Perimetre.builder().code("DAILY_SUIVI_PILOTE").label("Daily suivi pilote").build(),
            Perimetre.builder().code("DATA_DICTIONARY").label("Data Dictionnary").build(),
            Perimetre.builder().code("DESIGN_EXECUTABLE").label("Design exécutable").build(),
            Perimetre.builder().code("DESIGN_MEETING").label("Design meeting").build(),
            Perimetre.builder().code("DEV").label("Dev").build(),
            Perimetre.builder().code("ECRITURE_VALIDATION_US").label("Ecriture/Validation des US").build(),
            Perimetre.builder().code("EVOL_8_8").label("Evol 8.8").build(),
            Perimetre.builder().code("FORMATION").label("Formation").build(),
            Perimetre.builder().code("MEETING").label("Meeting").build(),
            Perimetre.builder().code("MIGRATION").label("migration").build(),
            Perimetre.builder().code("MODELISATION_V0_DI").label("Modélisation V0 DI").build(),
            Perimetre.builder().code("MODELISATION_V0_IRU").label("Modélisation V0 IRU DOLMEN").build(),
            Perimetre.builder().code("MODELISATION_V0_OTOBO").label("Modélisation V0 OTOBO").build(),
            Perimetre.builder().code("MODELISATION_V0_VDR").label("Modélisation V0 VDR").build(),
            Perimetre.builder().code("ORIZON").label("Orizon").build(),
            Perimetre.builder().code("PARAMETRAGE").label("Paramétrage").build(),
            Perimetre.builder().code("PARAM_TESTS_DI").label("Paramétrage & Tests Uni DI").build(),
            Perimetre.builder().code("PARAM_TESTS_IRU").label("Paramétrage & Tests Uni IRU DOLMEN").build(),
            Perimetre.builder().code("PARAM_TESTS_LP68_2").label("Paramétrage & Tests Uni LP 68.2").build(),
            Perimetre.builder().code("PARAM_TESTS_OTOBO").label("Paramétrage & Tests Uni OTOBO").build(),
            Perimetre.builder().code("PARAM_TESTS_RENO").label("Paramétrage & Tests Uni RENO").build(),
            Perimetre.builder().code("PARAM_TESTS_VDR").label("Paramétrage & Tests Uni VDR").build(),
            Perimetre.builder().code("PARAMETRAGE_MEETING").label("Paramétrage meeting").build(),
            Perimetre.builder().code("PASSATION").label("Passation").build(),
            Perimetre.builder().code("POINT_COPROJ").label("Point COPROJ").build(),
            Perimetre.builder().code("POINT_SUIVI_SYNCHRO").label("Point de suivi/synchro").build(),
            Perimetre.builder().code("POINT_IT_ITRES").label("Point IT (ITRES)").build(),
            Perimetre.builder().code("POINT_PO_SYNCHRO").label("Point PO Synchro Commun").build(),
            Perimetre.builder().code("POINT_SYNCHRO_MID").label("Point Synchro MID").build(),
            Perimetre.builder().code("PREP_MIGRATION").label("Préparation migration").build(),
            Perimetre.builder().code("PREP_PILOTES").label("Préparation Pilotes").build(),
            Perimetre.builder().code("PROCESS_IDENTITY").label("Process Identity").build(),
            Perimetre.builder().code("REDACTION").label("Rédaction").build(),
            Perimetre.builder().code("REDACTION_LIVRABLE").label("Rédaction livrable").build(),
            Perimetre.builder().code("REFERENTIEL_VARIABLES").label("Référentiel des variables").build(),
            Perimetre.builder().code("REQUETAGE").label("Requêtage").build(),
            Perimetre.builder().code("RESOLUTION_CENTER").label("Resolution Center").build(),
            Perimetre.builder().code("SPRINT_DAILY").label("Sprint Daily").build(),
            Perimetre.builder().code("SPRINT_PLANNING").label("Sprint planning").build(),
            Perimetre.builder().code("SPRINT_RETRO").label("Sprint Retro").build(),
            Perimetre.builder().code("SPRINT_REVIEW").label("Sprint Review").build(),
            Perimetre.builder().code("SUIVI_PILOTE").label("Suivi pilote").build(),
            Perimetre.builder().code("TEST_INTEG_OPS_DCI").label("Test intégration OPS/DCI").build(),
            Perimetre.builder().code("TEST_INTEG_VDR_OPS_DCI").label("Test intégration VDR avec OPS/DCI").build(),
            Perimetre.builder().code("TESTPILOT").label("TestPilot").build(),
            Perimetre.builder().code("TESTS_B2B_DI").label("Tests B2B DI").build(),
            Perimetre.builder().code("TESTS_B2B_IRU").label("Tests B2B IRU DOLMEN").build(),
            Perimetre.builder().code("TESTS_B2B_LP68_05").label("Tests B2B LP 68.05").build(),
            Perimetre.builder().code("TESTS_B2B_OTOBO").label("Tests B2B OTOBO").build(),
            Perimetre.builder().code("TESTS_B2B_RENO").label("Tests B2B RENO").build(),
            Perimetre.builder().code("TESTS_B2B_VDR").label("Tests B2B VDR").build(),
            Perimetre.builder().code("VALIDATION_LIVRABLE").label("Validation livrable").build(),
            Perimetre.builder().code("WORKSHOP_SPEC").label("Workshop SPEC technique & Fonctionnel").build()
        ));
        log.info("Périmètres initialisés.");
    }

    // ============================================================
    // 5 — PHASES
    // ============================================================
    private void initPhases() {
        if (phaseRepository.count() > 0) return;
        log.info("Initialisation des phases...");
        phaseRepository.saveAll(List.of(
            Phase.builder().code("AUTORISATION").label("Autorisation").build(),
            Phase.builder().code("AUTOSENSE").label("Autosense").build(),
            Phase.builder().code("BI").label("BI").build(),
            Phase.builder().code("BOOTCAMP").label("Bootcamp").build(),
            Phase.builder().code("CADRAGE_DESIGN").label("Cadrage / Design exécutable").build(),
            Phase.builder().code("DEV").label("dev").build(),
            Phase.builder().code("DOCUMENTATION").label("Documentation").build(),
            Phase.builder().code("FORMATION").label("Formation").build(),
            Phase.builder().code("MCO").label("MCO").build(),
            Phase.builder().code("MEETING").label("Meeting").build(),
            Phase.builder().code("MIGRATION_MES").label("Migration + MES").build(),
            Phase.builder().code("ORIZON").label("Orizon").build(),
            Phase.builder().code("PARAMETRAGE").label("Paramétrage").build(),
            Phase.builder().code("PILOTES").label("Pilotes").build(),
            Phase.builder().code("PREP_MIGRATION").label("Préparation migration").build(),
            Phase.builder().code("PREP_PILOTES").label("Préparation pilotes").build(),
            Phase.builder().code("REQUETAGE").label("Requêtage").build(),
            Phase.builder().code("TESTS_B2B").label("Tests B2B").build(),
            Phase.builder().code("US").label("US").build(),
            Phase.builder().code("VSR").label("VSR").build()
        ));
        log.info("Phases initialisées.");
    }

    // ============================================================
    // 6 — LPs (Lots de Programme)
    // ============================================================
    private void initLPs() {
        if (lpRepository.count() > 0) return;
        log.info("Initialisation des LPs...");
        lpRepository.saveAll(List.of(
            LP.builder().code("LP_68_2").label("68.2").build(),
            LP.builder().code("LP_68_5").label("68.5").build(),
            LP.builder().code("AUTORISATION").label("Autorisation").build(),
            LP.builder().code("AUTOSENSE").label("Autosense").build(),
            LP.builder().code("BOOTCAMP").label("Bootcamp").build(),
            LP.builder().code("CELONIS").label("Celonis").build(),
            LP.builder().code("DEMONTAGE_COUCHE").label("Démontage de couche").build(),
            LP.builder().code("DEMONTAGE_FH").label("Démontage FH").build(),
            LP.builder().code("DEMONTAGE_RADIO").label("Démontage Radio").build(),
            LP.builder().code("DEMONTAGE_ROUT_TCAP").label("Démontage Rout TCap").build(),
            LP.builder().code("DI_CROZON").label("DI Crozon").build(),
            LP.builder().code("DI_CSG").label("DI CSG").build(),
            LP.builder().code("DI_RAN4").label("DI Ran 4").build(),
            LP.builder().code("DI_ZTD").label("DI ZTD").build(),
            LP.builder().code("FORMATION").label("Formation").build(),
            LP.builder().code("GUP").label("GUP").build(),
            LP.builder().code("IRU_DOLMEN").label("IRU DOLMEN").build(),
            LP.builder().code("MIGRATION_NRA_O_CTR").label("Migration NRA/O vers CTR").build(),
            LP.builder().code("MIGRATION_TCAP_FO").label("Migration TCap FO Tierce").build(),
            LP.builder().code("MIGRATION_TCAP_FTTA").label("Migration TCap FTTA").build(),
            LP.builder().code("MO_PROV_CIBLE").label("MO Prov / Cible").build(),
            LP.builder().code("ORIZON").label("Orizon").build(),
            LP.builder().code("PMG").label("PMG").build(),
            LP.builder().code("RENO").label("RENO").build(),
            LP.builder().code("RENO_ZS_PROD_EXPERTE").label("RENO ZS/Prod Experte").build(),
            LP.builder().code("RENO_ZTD_CZ_RAN4").label("RENO ZTD Cz Ran 4").build(),
            LP.builder().code("UIPATH").label("uiPath").build(),
            LP.builder().code("VDR").label("VDR").build(),
            LP.builder().code("VDR_FH").label("VDR FH").build(),
            LP.builder().code("VDR_ZTD_HZTD").label("VDR ZTD/HZTD").build(),
            LP.builder().code("VERCORS").label("Vercors").build()
        ));
        log.info("LPs initialisés.");
    }
}
