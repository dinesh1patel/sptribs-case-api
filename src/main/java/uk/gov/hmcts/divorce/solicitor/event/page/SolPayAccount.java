package uk.gov.hmcts.divorce.solicitor.event.page;

import uk.gov.hmcts.divorce.ciccase.model.Application;
import uk.gov.hmcts.divorce.ciccase.model.CaseData;
import uk.gov.hmcts.divorce.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.divorce.common.ccd.PageBuilder;

public class SolPayAccount implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {

        pageBuilder
            .page("SolPayAccount")
            .pageLabel("Pay on account")
            .showCondition("solPaymentHowToPay=\"feePayByAccount\"")
            .complex(CaseData::getApplication)
                .mandatoryWithLabel(Application::getPbaNumbers, "Select your account number")
                .mandatoryWithLabel(Application::getFeeAccountReference, "Enter your payment reference")
                .done();
    }
}
