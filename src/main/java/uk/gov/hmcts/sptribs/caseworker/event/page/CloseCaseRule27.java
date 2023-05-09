package uk.gov.hmcts.sptribs.caseworker.event.page;

import uk.gov.hmcts.sptribs.caseworker.model.CloseCase;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.sptribs.common.ccd.PageBuilder;

import java.util.HashMap;
import java.util.Map;

public class CloseCaseRule27 implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        Map<String, String> map = new HashMap<>();
        map.put("closeCaseWithdrawalDetails", "closeCloseCaseReason = \"caseWithdrawn\"");
        map.put("closeCaseRejectionDetails", "closeCloseCaseReason = \"caseRejected\"");
        map.put("closeCaseStrikeOutDetails", "closeCloseCaseReason = \"caseStrikeOut\"");
        map.put("closeCaseConcessionDetails", "closeCloseCaseReason = \"caseConcession\"");
        map.put("closeCaseConsentOrder", "closeCloseCaseReason = \"consentOrder\"");
        map.put("closeCaseRule27", "closeCloseCaseReason = \"rule27\"");
        pageBuilder.page("closeCaseRule27")
            .pageLabel("Rule 27 details")
            .pageShowConditions(map)
            .complex(CaseData::getCloseCase)
            .mandatory(CloseCase::getRule27DecisionDate)
            .done();
    }
}