package uk.gov.hmcts.sptribs.caseworker.event;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CCDConfig;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.CaseLink;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.ccd.client.model.SubmittedCallbackResponse;
import uk.gov.hmcts.sptribs.caseworker.model.CaseLinks;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.ciccase.model.CicCase;
import uk.gov.hmcts.sptribs.ciccase.model.State;
import uk.gov.hmcts.sptribs.ciccase.model.UserRole;
import uk.gov.hmcts.sptribs.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.sptribs.caseworker.util.EventConstants.CASEWORKER_LINK_CASE;
import static uk.gov.hmcts.sptribs.ciccase.model.State.AwaitingHearing;
import static uk.gov.hmcts.sptribs.ciccase.model.State.AwaitingOutcome;
import static uk.gov.hmcts.sptribs.ciccase.model.State.CaseManagement;
import static uk.gov.hmcts.sptribs.ciccase.model.State.Submitted;
import static uk.gov.hmcts.sptribs.ciccase.model.UserRole.COURT_ADMIN_CIC;
import static uk.gov.hmcts.sptribs.ciccase.model.UserRole.SOLICITOR;
import static uk.gov.hmcts.sptribs.ciccase.model.UserRole.SUPER_USER;
import static uk.gov.hmcts.sptribs.ciccase.model.access.Permissions.CREATE_READ_UPDATE_DELETE;

@Component
@Slf4j
public class CaseWorkerLinkCase implements CCDConfig<CaseData, State, UserRole> {

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        PageBuilder pageBuilder = new PageBuilder(configBuilder
            .event(CASEWORKER_LINK_CASE)
            .forStates(Submitted, CaseManagement, AwaitingHearing, AwaitingOutcome)
            .name("Links: Link case")
            .showSummary()
            .description("Links: Link case")
            .aboutToSubmitCallback(this::aboutToSubmit)
            .submittedCallback(this::submitted)
            .grant(CREATE_READ_UPDATE_DELETE, COURT_ADMIN_CIC, SUPER_USER)
            .grantHistoryOnly(SOLICITOR));

        addWarning(pageBuilder);
        addSelectCase(pageBuilder);
    }

    @SneakyThrows
    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(CaseDetails<CaseData, State> details,
                                                                       CaseDetails<CaseData, State> beforeDetails) {
        log.info("Caseworker link the case callback invoked for Case Id: {}", details.getId());
        var data = details.getData();
        // var linkCase = data.getLinkCase();
        /*    LinkReason linkReason = LinkReason.builder()
            .reason(linkCase.getLinkCaseReason().getLabel())
            .otherDescription(linkCase.getLinkCaseOtherDescription())
            .build();

 */
        CaseLinks caseLink = CaseLinks.builder()
            .caseReference(data.getCicCase().getLinkCaseNumber())
            .reason(data.getCicCase().getLinkCaseReason().getLabel())
            .otherDescription(data.getCicCase().getLinkCaseOtherDescription())
            // .reasonForLink(linkReason)
            .build();
        //CaseLinksElement<CaseLink> element = new CaseLinksElement<>(data.getCicCase().getLinkCaseNumber(), caseLink);
        ListValue<CaseLinks> listValue=new ListValue<>();
        listValue.setValue(caseLink);

        if (null == data.getCicCase().getCaseLinks()) {
            List<ListValue<CaseLinks>> list = new ArrayList<>();
            data.getCicCase().setCaseLinks(list);
        }
          data.getCicCase().getCaseLinks().add(listValue);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .state(details.getState())
            .build();
    }

    public SubmittedCallbackResponse submitted(CaseDetails<CaseData, State> details,
                                               CaseDetails<CaseData, State> beforeDetails) {
        return SubmittedCallbackResponse.builder()
            .confirmationHeader("Case Updated")
            .build();
    }

    private void addWarning(PageBuilder pageBuilder) {
        pageBuilder.page("beforeYouStart")
            .pageLabel("Before you start")
            .label("beforeYouStartLabel",
                """
                    If a group of linked cases has a lead case, you must start from the lead case.

                    If the cases to be linked has no lead, you can start the linking journey from any of those cases
                    """);
    }

    private void addSelectCase(PageBuilder pageBuilder) {
        pageBuilder.page("selectCase")
            .pageLabel("Select a case you want to link to this case")
            .complex(CaseData::getCicCase)
            .mandatory(CicCase::getLinkCaseNumber)
            .mandatoryWithLabel(CicCase::getLinkCaseReason, "Select all that apply")
         //   .mandatory(CicCase::getLinkCaseOtherDescription, "linkLinkCaseReason = \"other\"")
            .done();
    }

}
