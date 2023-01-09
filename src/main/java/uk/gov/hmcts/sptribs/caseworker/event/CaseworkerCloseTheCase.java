package uk.gov.hmcts.sptribs.caseworker.event;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.reform.ccd.client.model.SubmittedCallbackResponse;
import uk.gov.hmcts.sptribs.caseworker.event.page.CloseCaseWarning;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.ciccase.model.State;
import uk.gov.hmcts.sptribs.ciccase.model.UserRole;
import uk.gov.hmcts.sptribs.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.sptribs.common.ccd.PageBuilder;
import uk.gov.hmcts.sptribs.common.notification.CaseCloseNotification;

import static java.lang.String.format;
import static uk.gov.hmcts.sptribs.ciccase.model.State.CaseClosed;
import static uk.gov.hmcts.sptribs.ciccase.model.State.CaseManagement;
import static uk.gov.hmcts.sptribs.ciccase.model.UserRole.COURT_ADMIN_CIC;
import static uk.gov.hmcts.sptribs.ciccase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.sptribs.ciccase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.sptribs.ciccase.model.access.Permissions.CREATE_READ_UPDATE_DELETE;

@Component
@Slf4j
public class CaseworkerCloseTheCase implements CCDConfig<CaseData, State, UserRole> {
    public static final String CASEWORKER_CLOSE_THE_CASE = "caseworker-close-the-case";

    private static final CcdPageConfiguration closeCaseWarning = new CloseCaseWarning();

    @Autowired
    CaseCloseNotification caseCloseNotification;


    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {

        var pageBuilder = closeCase(configBuilder);
        closeCaseWarning.addTo(pageBuilder);

    }

    public PageBuilder closeCase(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        return new PageBuilder(configBuilder
            .event(CASEWORKER_CLOSE_THE_CASE)
            .forStates(CaseManagement)
            .name("Close the Case")
            .showSummary()
            .description("Close the case")
            .aboutToSubmitCallback(this::aboutToSubmit)
            .submittedCallback(this::closed)
            .showEventNotes()
            .grant(CREATE_READ_UPDATE_DELETE, COURT_ADMIN_CIC, SUPER_USER)
            .grantHistoryOnly(SOLICITOR));
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        final CaseDetails<CaseData, State> details,
        final CaseDetails<CaseData, State> beforeDetails
    ) {
        log.info("Caseworker close the case callback invoked for Case Id: {}", details.getId());
        var caseData = details.getData();
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .state(CaseClosed)
            .build();
    }

    public SubmittedCallbackResponse closed(CaseDetails<CaseData, State> details,
                                            CaseDetails<CaseData, State> beforeDetails) {
        var data = details.getData();
        var cicCase = data.getCicCase();
        String caseNumber = data.getHyphenatedCaseRef();
        final StringBuilder messageLine2 = new StringBuilder(100);
        messageLine2.append(" A notification will be sent  to: ");
        if (!CollectionUtils.isEmpty(cicCase.getNotifyPartySubject())) {
            messageLine2.append("Subject, ");
            caseCloseNotification.sendToSubject(details.getData(), caseNumber);
        }
        if (!CollectionUtils.isEmpty(cicCase.getNotifyPartyRepresentative())) {
            messageLine2.append("Representative, ");
            caseCloseNotification.sendToRepresentative(details.getData(), caseNumber);
        }
        messageLine2.append("Respondent, ");
        caseCloseNotification.sendToRespondent(details.getData(), caseNumber);

        return SubmittedCallbackResponse.builder()
            .confirmationHeader(format("# Case closed %n##  This case has now been closed. %n##"
                + "  %s ", messageLine2.substring(0, messageLine2.length() - 2)))
            .build();
    }
}
