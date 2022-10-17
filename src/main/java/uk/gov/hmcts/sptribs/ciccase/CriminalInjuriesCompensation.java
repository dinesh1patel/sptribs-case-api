package uk.gov.hmcts.sptribs.ciccase;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.ciccase.model.RetiredFields;
import uk.gov.hmcts.sptribs.ciccase.model.State;
import uk.gov.hmcts.sptribs.ciccase.model.UserRole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CriminalInjuriesCompensation implements CCDConfig<CaseData, State, UserRole> {
    public static final String CASE_TYPE = "CIC";
    public static final String JURISDICTION = "DIVORCE";
    private static final String CREATE_READ_UPDATE = "CRU";
    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.addPreEventHook(RetiredFields::migrate);
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://localhost:4013"));

        configBuilder.caseType(CASE_TYPE, "CIC Case Type", "Handling of the dissolution of marriage");
        configBuilder.jurisdiction(JURISDICTION, "CIC", "Family Divorce: dissolution of marriage");

        configBuilder.caseRoleToAccessProfile(UserRole.CREATOR).caseAccessCategories("SPEC").accessProfiles(UserRole.CREATOR.getRole()).authorisation(CREATE_READ_UPDATE);
        configBuilder.caseRoleToAccessProfile(UserRole.CASE_OFFICER_CIC).caseAccessCategories("SPEC").accessProfiles(UserRole.CASE_OFFICER_CIC.getRole()).authorisation(CREATE_READ_UPDATE);
        configBuilder.caseRoleToAccessProfile(UserRole.SYSTEMUPDATE).caseAccessCategories("SPEC").accessProfiles(UserRole.SYSTEMUPDATE.getRole()).authorisation(CREATE_READ_UPDATE);
        configBuilder.caseRoleToAccessProfile(UserRole.SOLICITOR).caseAccessCategories("SPEC").accessProfiles(UserRole.SOLICITOR.getRole()).authorisation(CREATE_READ_UPDATE);
        configBuilder.caseRoleToAccessProfile(UserRole.SUPER_USER_CIC).caseAccessCategories("UNSPEC").accessProfiles(UserRole.SUPER_USER_CIC.getRole()).authorisation(CREATE_READ_UPDATE);
        configBuilder.caseRoleToAccessProfile(UserRole.COURT_ADMIN_CIC).caseAccessCategories("UNSPEC").accessProfiles(UserRole.COURT_ADMIN_CIC.getRole()).authorisation(CREATE_READ_UPDATE);
        configBuilder.caseRoleToAccessProfile(UserRole.CASE_OFFICER_CIC).caseAccessCategories("UNSPEC").accessProfiles(UserRole.CASE_OFFICER_CIC.getRole()).authorisation(CREATE_READ_UPDATE);
        configBuilder.caseRoleToAccessProfile(UserRole.DISTRICT_REGISTRAR_CIC).caseAccessCategories("UNSPEC").accessProfiles(UserRole.DISTRICT_REGISTRAR_CIC.getRole()).authorisation(CREATE_READ_UPDATE);
        configBuilder.caseRoleToAccessProfile(UserRole.DISTRICT_JUDGE_CIC).caseAccessCategories("UNSPEC").accessProfiles(UserRole.DISTRICT_JUDGE_CIC.getRole()).authorisation(CREATE_READ_UPDATE);
        configBuilder.caseRoleToAccessProfile(UserRole.RESPONDENT_CIC).caseAccessCategories("UNSPEC").accessProfiles(UserRole.RESPONDENT_CIC.getRole()).authorisation(CREATE_READ_UPDATE);
        configBuilder.caseRoleToAccessProfile(UserRole.CITIZEN_CIC).caseAccessCategories("UNSPEC").accessProfiles(UserRole.CITIZEN_CIC.getRole()).authorisation("C");
        // to shutter the service within xui uncomment this line
        // configBuilder.shutterService();
        log.info("Building definition for " + System.getenv().getOrDefault("ENVIRONMENT", ""));
    }
}
