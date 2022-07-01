package uk.gov.hmcts.divorce.common.event;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.divorce.ciccase.model.CaseData;
import uk.gov.hmcts.divorce.ciccase.model.State;
import uk.gov.hmcts.divorce.ciccase.model.UserRole;
import uk.gov.hmcts.divorce.common.ccd.PageBuilder;

import static uk.gov.hmcts.divorce.ciccase.model.State.Holding;
import static uk.gov.hmcts.divorce.ciccase.model.UserRole.APPLICANT_2_SOLICITOR;
import static uk.gov.hmcts.divorce.ciccase.model.UserRole.CASE_WORKER;
import static uk.gov.hmcts.divorce.ciccase.model.UserRole.LEGAL_ADVISOR;
import static uk.gov.hmcts.divorce.ciccase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.divorce.ciccase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.divorce.ciccase.model.access.Permissions.CREATE_READ_UPDATE;

@Component
public class ConfirmReceipt implements CCDConfig<CaseData, State, UserRole> {
    public static final String CONFIRM_RECEIPT = "confirm-receipt";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        new PageBuilder(configBuilder
            .event(CONFIRM_RECEIPT)
            .forStates(Holding)
            .showCondition("applicationType=\"jointApplication\"")
            .name("Confirm Receipt")
            .description("Confirm Receipt")
            .showSummary()
            .showEventNotes()
            .grant(CREATE_READ_UPDATE, SOLICITOR, APPLICANT_2_SOLICITOR, CASE_WORKER)
            .grantHistoryOnly(SUPER_USER, LEGAL_ADVISOR));
    }
}
