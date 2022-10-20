package uk.gov.hmcts.sptribs.cases.workbasket;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.sptribs.cases.model.CaseData;
import uk.gov.hmcts.sptribs.cases.model.State;
import uk.gov.hmcts.sptribs.cases.model.UserRole;

import static uk.gov.hmcts.sptribs.cases.search.SearchResultFields.SEARCH_RESULT_FIELD_LIST;

@Component
public class WorkBasketResultFields implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        configBuilder
            .workBasketResultFields()
            .fields(SEARCH_RESULT_FIELD_LIST);
    }
}
