package uk.gov.hmcts.divorce.common.event.page;

import uk.gov.hmcts.divorce.ciccase.model.Applicant;
import uk.gov.hmcts.divorce.ciccase.model.CaseData;
import uk.gov.hmcts.divorce.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.divorce.common.ccd.PageBuilder;

public class Applicant2SolAosOtherProceedings implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder
            .page("Applicant2SolAosOtherProceedings")
            .pageLabel("Other legal proceedings")
            .complex(CaseData::getApplicant2)
            .mandatory(Applicant::getLegalProceedings)
            .mandatory(Applicant::getLegalProceedingsDetails, "applicant2LegalProceedings=\"Yes\"")
            .done();
    }
}
