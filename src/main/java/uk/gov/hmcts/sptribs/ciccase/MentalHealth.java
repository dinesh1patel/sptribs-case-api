package uk.gov.hmcts.sptribs.ciccase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.sptribs.ciccase.model.RetiredFields;
import uk.gov.hmcts.sptribs.ciccase.model.State;
import uk.gov.hmcts.sptribs.ciccase.model.UserRole;
import uk.gov.hmcts.sptribs.ciccase.model.casetype.MentalHealthData;
import uk.gov.hmcts.sptribs.common.ccd.CcdCaseType;
import uk.gov.hmcts.sptribs.common.ccd.CcdServiceCode;

import static uk.gov.hmcts.sptribs.ciccase.search.SearchInputFields.SEARCH_FIELD_LIST;
import static uk.gov.hmcts.sptribs.ciccase.search.SearchResultFields.SEARCH_RESULT_FIELD_LIST;


@Component
@Slf4j
public class MentalHealth implements CCDConfig<MentalHealthData, State, UserRole> {

    public static final String CASE_TYPE = "MH";
    public static final String CASE_TYPE_SHORT_NAME = "MH Case type";
    public static final String JURISDICTION = "ST_MH";
    public static final String JURISDICTION_SHORT_NAME = "MH";

    @Override
    public void configure(final ConfigBuilder<MentalHealthData, State, UserRole> configBuilder) {
        // Each case type must define these mandatory bits of config.
        configBuilder.searchInputFields().fields(SEARCH_FIELD_LIST);
        configBuilder.searchResultFields().fields(SEARCH_RESULT_FIELD_LIST);
        configBuilder.workBasketResultFields().fields(SEARCH_RESULT_FIELD_LIST);

        configBuilder.addPreEventHook(RetiredFields::migrate);
        configBuilder.setCallbackHost(System.getenv().getOrDefault("CASE_API_URL", "http://localhost:4013"));

        configBuilder.caseType(CASE_TYPE, CASE_TYPE_SHORT_NAME, CcdCaseType.MH.getDescription());
        configBuilder.jurisdiction(JURISDICTION, JURISDICTION_SHORT_NAME, CcdServiceCode.MH.getCcdServiceDescription());

        configBuilder.event("test")
            .forState(State.AwaitingApplicant1Response)
            .name("Test event")
            .fields()
            .mandatory(MentalHealthData::getHearingDate);


        // to shutter the service within xui uncomment this line
        // configBuilder.shutterService();
        log.info("Building definition for " + System.getenv().getOrDefault("ENVIRONMENT", ""));
    }
}
