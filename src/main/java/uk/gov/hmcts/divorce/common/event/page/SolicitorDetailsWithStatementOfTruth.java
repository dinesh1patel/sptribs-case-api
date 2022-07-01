package uk.gov.hmcts.divorce.common.event.page;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.divorce.ciccase.model.AcknowledgementOfService;
import uk.gov.hmcts.divorce.ciccase.model.CaseData;
import uk.gov.hmcts.divorce.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.divorce.common.ccd.PageBuilder;

@Component
public class SolicitorDetailsWithStatementOfTruth implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {

        pageBuilder
            .page("SubmitAos")
            .pageLabel("Statement of Truth - Solicitor")
            .complex(CaseData::getAcknowledgementOfService)
                .label("labelApplicant2SolStatementOfTruth-SOT", "## Statement of truth")
                .mandatoryWithLabel(AcknowledgementOfService::getStatementOfTruth,
                    "I am duly authorised by the respondent to sign this statement.")
                .mandatory(AcknowledgementOfService::getSolicitorName)
                .mandatory(AcknowledgementOfService::getSolicitorFirm)
                .optional(AcknowledgementOfService::getAdditionalComments)
            .done()
            .label("warning-ProceedingForContent",
                "*Proceedings for contempt of court may be brought against anyone who makes, or causes to be made, "
                    + "a false statement verified by a statement of truth without an honest belief in its truth*");
    }
}
