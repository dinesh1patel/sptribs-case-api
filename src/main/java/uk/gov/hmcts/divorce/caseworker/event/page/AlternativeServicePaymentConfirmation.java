package uk.gov.hmcts.divorce.caseworker.event.page;

import uk.gov.hmcts.divorce.ciccase.model.AlternativeService;
import uk.gov.hmcts.divorce.ciccase.model.CaseData;
import uk.gov.hmcts.divorce.ciccase.model.FeeDetails;
import uk.gov.hmcts.divorce.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.divorce.common.ccd.PageBuilder;

public class AlternativeServicePaymentConfirmation implements CcdPageConfiguration  {

    @Override
    public void addTo(PageBuilder pageBuilder) {

        pageBuilder.page("alternativeServicePayment")
            .pageLabel("Payment - service application payment")
            .complex(CaseData::getAlternativeService)
                .complex(AlternativeService::getServicePaymentFee)
                .mandatory(FeeDetails::getPaymentMethod)
                .mandatory(FeeDetails::getAccountNumber, "servicePaymentFeePaymentMethod = \"feePayByAccount\"")
                .optional(FeeDetails::getAccountReferenceNumber, "servicePaymentFeePaymentMethod = \"feePayByAccount\"")
                .mandatory(FeeDetails::getHelpWithFeesReferenceNumber, "servicePaymentFeePaymentMethod = \"feePayByHelp\"")
                .done()
            .done();
    }
}
