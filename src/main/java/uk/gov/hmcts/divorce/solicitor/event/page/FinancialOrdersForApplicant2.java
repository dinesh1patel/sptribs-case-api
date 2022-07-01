package uk.gov.hmcts.divorce.solicitor.event.page;

import uk.gov.hmcts.divorce.ciccase.model.Applicant;
import uk.gov.hmcts.divorce.ciccase.model.CaseData;
import uk.gov.hmcts.divorce.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.divorce.common.ccd.PageBuilder;

public class FinancialOrdersForApplicant2 implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {

        pageBuilder
            .page("FinancialOrdersForApplicant2")
            .pageLabel("Financial orders")
            .complex(CaseData::getApplicant2)
                .mandatoryWithLabel(Applicant::getFinancialOrder,"Does Applicant 2 wish to apply for a financial order?")
                .mandatory(Applicant::getFinancialOrdersFor, "applicant2FinancialOrder=\"Yes\"")
                .done();
    }
}
