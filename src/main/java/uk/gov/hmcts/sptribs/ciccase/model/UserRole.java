package uk.gov.hmcts.sptribs.ciccase.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasRole;

@AllArgsConstructor
@Getter
public enum UserRole implements HasRole {

    // Common User roles
    //TODO: update : SUPER_USER, SOLICITOR before prod deploy
    SUPER_USER("caseworker-sptribs-superuser", "CRU"),
    SYSTEMUPDATE("caseworker-sptribs-systemupdate", "CRU"),
    SOLICITOR("caseworker", "CRU"),
    CREATOR("[CREATOR]", "CRU"),

    // CIC User roles
    COURT_ADMIN_CIC("caseworker-sptribs-cic-courtadmin", "CRU"),
    CASE_OFFICER_CIC("caseworker-sptribs-cic-caseofficer", "CRU"),
    DISTRICT_REGISTRAR_CIC("caseworker-sptribs-cic-districtregistrar", "CRU"),
    DISTRICT_JUDGE_CIC("caseworker-sptribs-cic-districtjudge", "CRU"),
    RESPONDENT_CIC("caseworker-sptribs-cic-respondent", "CRU"),

    ST_CIC_CASEWORKER("caseworker-st_cic-caseworker", "CRU"),
    ST_CIC_SENIOR_CASEWORKER("caseworker-st_cic-senior-caseworker", "CRU"),
    ST_CIC_HEARING_CENTRE_ADMIN("caseworker-st_cic-hearing-centre-admin", "CRU"),
    ST_CIC_HEARING_CENTRE_TEAM_LEADER("caseworker-st_cic-hearing-centre-team-leader", "CRU"),
    ST_CIC_SENIOR_JUDGE("caseworker-st_cic-senior-judge", "CRU"),
    ST_CIC_JUDGE("caseworker-st_cic-judge", "CRU"),
    ST_CIC_RESPONDENT("caseworker-st_cic-respondent", "CRU"),

    CITIZEN_CIC("citizen-sptribs-cic-dss", "C"),

    //Access profile matching roles
    CASE_ALLOCATOR("case-allocator", "R"),
    HEARING_CENTRE_ADMIN("hearing-centre-admin", "CRU"),
    HEARING_CENTRE_TEAM_LEADER("hearing-centre-team-leader", "CRU"),
    HMCTS_ADMIN("hmcts-admin", "R"),
    HMCTS_CTSC("hmcts-ctsc", "R"),
    HMCTS_JUDICIARY("hmcts-judiciary", "R"),
    HMCTS_LEGAL_OPERATIONS("hmcts-legal-operations", "R"),
    JUDGE("judge", "CRU"),
    MEDICAL("medical", "CRU"),
    REGIONAL_CENTRE_ADMIN("regional-centre-admin", "CRU"),
    REGIONAL_CENTRE_TEAM_LEADER("regional-centre-team-leader", "CRU"),
    RESPONDENT("respondent", "CRU"),
    SENIOR_JUDGE("senior-judge", "CRU"),
    SENIOR_TRIBUNAL_CASEWORKER("senior-tribunal-caseworker", "CRU"),
    SPECIFIC_ACCESS_APPROVER_ADMIN("specific-access-approver-admin", "R"),
    SPECIFIC_ACCESS_APPROVER_JUDICIARY("specific-access-approver-judiciary", "R"),
    SPECIFIC_ACCESS_APPROVER_LEGAL_OPERATIONS("specific-access-approver-legal-operations", "R"),
    SPECIFIC_ACCESS_APPROVER_CTSC("specific-access-approver-ctsc", "R"),
    TASK_SUPERVISOR("task-supervisor", "R"),
    TRIBUNAL_CASEWORKER("tribunal-caseworker", "CRU"),
    HEARING_JUDGE("hearing-judge", "CRU"),
    INTERLOC_JUDGE("interloc-judge", "CRU");


    @JsonValue
    private final String role;
    private final String caseTypePermissions;
}
