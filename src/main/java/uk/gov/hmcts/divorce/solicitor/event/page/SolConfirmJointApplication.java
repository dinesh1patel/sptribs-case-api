package uk.gov.hmcts.divorce.solicitor.event.page;

import uk.gov.hmcts.divorce.ciccase.model.Applicant;
import uk.gov.hmcts.divorce.ciccase.model.Application;
import uk.gov.hmcts.divorce.ciccase.model.CaseData;
import uk.gov.hmcts.divorce.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.divorce.common.ccd.PageBuilder;

public class SolConfirmJointApplication implements CcdPageConfiguration {

    @Override
    public void addTo(final PageBuilder pageBuilder) {
        pageBuilder
            .page("ConfirmJointApplication")
            .pageLabel("Confirm Joint Application")
            .complex(CaseData::getApplicant2)
                .readonly(Applicant::getSolicitorRepresented, NEVER_SHOW)
            .done()
            .showCondition("applicationType=\"jointApplication\" AND applicant2SolicitorRepresented=\"Yes\"")
            .complex(CaseData::getApplication)
            .readonly(Application::getApplicant2SolicitorAnswersLink)
            .done();
    }
}
