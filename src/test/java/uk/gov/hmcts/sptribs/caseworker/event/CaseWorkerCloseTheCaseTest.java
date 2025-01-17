package uk.gov.hmcts.sptribs.caseworker.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.reform.ccd.client.model.SubmittedCallbackResponse;
import uk.gov.hmcts.sptribs.caseworker.model.CloseCase;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.ciccase.model.CicCase;
import uk.gov.hmcts.sptribs.ciccase.model.RepresentativeCIC;
import uk.gov.hmcts.sptribs.ciccase.model.RespondentCIC;
import uk.gov.hmcts.sptribs.ciccase.model.State;
import uk.gov.hmcts.sptribs.ciccase.model.SubjectCIC;
import uk.gov.hmcts.sptribs.ciccase.model.UserRole;
import uk.gov.hmcts.sptribs.common.notification.CaseWithdrawnNotification;
import uk.gov.hmcts.sptribs.document.model.CaseworkerCICDocument;
import uk.gov.hmcts.sptribs.document.model.DocumentType;
import uk.gov.hmcts.sptribs.judicialrefdata.JudicialService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.sptribs.document.DocumentConstants.DOCUMENT_VALIDATION_MESSAGE;
import static uk.gov.hmcts.sptribs.testutil.ConfigTestUtil.createCaseDataConfigBuilder;
import static uk.gov.hmcts.sptribs.testutil.ConfigTestUtil.getEventsFrom;
import static uk.gov.hmcts.sptribs.testutil.TestConstants.TEST_CASEWORKER_USER_EMAIL;
import static uk.gov.hmcts.sptribs.testutil.TestConstants.TEST_CASE_ID;
import static uk.gov.hmcts.sptribs.testutil.TestConstants.TEST_FIRST_NAME;
import static uk.gov.hmcts.sptribs.testutil.TestConstants.TEST_SOLICITOR_EMAIL;
import static uk.gov.hmcts.sptribs.testutil.TestConstants.TEST_SOLICITOR_NAME;
import static uk.gov.hmcts.sptribs.testutil.TestConstants.TEST_SUBJECT_EMAIL;
import static uk.gov.hmcts.sptribs.testutil.TestDataHelper.LOCAL_DATE_TIME;
import static uk.gov.hmcts.sptribs.testutil.TestDataHelper.closedCaseData;
import static uk.gov.hmcts.sptribs.testutil.TestDataHelper.getCaseworkerCICDocumentListWithFileFormat;
import static uk.gov.hmcts.sptribs.testutil.TestEventConstants.CASEWORKER_CLOSE_THE_CASE;

@ExtendWith(MockitoExtension.class)
class CaseWorkerCloseTheCaseTest {

    @InjectMocks
    private CaseworkerCloseTheCase caseworkerCloseTheCase;

    @Mock
    private JudicialService judicialService;

    @Mock
    private CaseWithdrawnNotification caseWithdrawnNotification;

    @Test
    void shouldAddConfigurationToConfigBuilder() {

        //Given
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

        //When
        caseworkerCloseTheCase.configure(configBuilder);

        //Then
        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_CLOSE_THE_CASE);
    }

    @Test
    void shouldRunAboutToStart() {
        //Given
        final CaseDetails<CaseData, State> updatedCaseDetails = new CaseDetails<>();
        final CicCase cicCase = CicCase.builder().build();
        final CaseData caseData = CaseData.builder()
            .cicCase(cicCase)
            .build();
        updatedCaseDetails.setData(caseData);
        DynamicList userList = new DynamicList();
        when(judicialService.getAllUsers()).thenReturn(userList);

        //When
        AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerCloseTheCase.aboutToStart(updatedCaseDetails);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.getData().getCloseCase().getRejectionName()).isEqualTo(userList);
        assertThat(response.getData().getCloseCase().getStrikeOutName()).isEqualTo(userList);
        assertThat(response.getData().getCurrentEvent()).isEqualTo(CASEWORKER_CLOSE_THE_CASE);
    }

    @Test
    void shouldSuccessfullyChangeCaseManagementStateToClosedState() {

        //Given
        final CaseData caseData = closedCaseData();
        final CaseworkerCICDocument caseworkerCICDocument = CaseworkerCICDocument.builder()
            .documentLink(Document.builder().build())
            .documentEmailContent("some email content")
            .documentCategory(DocumentType.LINKED_DOCS)
            .build();
        List<ListValue<CaseworkerCICDocument>> documentList = new ArrayList<>();
        ListValue<CaseworkerCICDocument> caseworkerCICDocumentListValue = new ListValue<>();
        caseworkerCICDocumentListValue.setValue(caseworkerCICDocument);
        documentList.add(caseworkerCICDocumentListValue);

        CloseCase closeCase = CloseCase.builder().documents(documentList).build();
        caseData.setCloseCase(closeCase);

        CicCase cicCase = CicCase.builder()
            .fullName(TEST_FIRST_NAME)
            .email(TEST_SUBJECT_EMAIL)
            .respondentEmail(TEST_CASEWORKER_USER_EMAIL)
            .representativeFullName(TEST_SOLICITOR_NAME)
            .representativeEmailAddress(TEST_SOLICITOR_EMAIL)
            .notifyPartyRepresentative(Set.of(RepresentativeCIC.REPRESENTATIVE))
            .notifyPartyRespondent(Set.of(RespondentCIC.RESPONDENT))
            .notifyPartySubject(Set.of(SubjectCIC.SUBJECT))
            .build();

        caseData.setCicCase(cicCase);
        final CaseDetails<CaseData, State> updatedCaseDetails = new CaseDetails<>();
        final CaseDetails<CaseData, State> beforeDetails = new CaseDetails<>();
        updatedCaseDetails.setData(caseData);
        updatedCaseDetails.setId(TEST_CASE_ID);
        updatedCaseDetails.setCreatedDate(LOCAL_DATE_TIME);

        //When
        assertThat(caseData.getCaseStatus()).isEqualTo(State.CaseManagement);
        AboutToStartOrSubmitResponse<CaseData, State> response =
            caseworkerCloseTheCase.aboutToSubmit(updatedCaseDetails, beforeDetails);

        SubmittedCallbackResponse closedCase =
            caseworkerCloseTheCase.closed(updatedCaseDetails, beforeDetails);

        //Then
        assertThat(closedCase).isNotNull();
        assertThat(closedCase.getConfirmationHeader()).contains("Case closed");
        assertThat(response.getState()).isEqualTo(State.CaseClosed);

    }

    @Test
    void shouldValidateUploadedDocument() {
        //Given
        final CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        CloseCase closeCase = CloseCase.builder().documents(getCaseworkerCICDocumentListWithFileFormat("xml")).build();
        final CaseData caseData = CaseData.builder()
            .closeCase(closeCase)
            .build();
        caseDetails.setData(caseData);

        //When
        final AboutToStartOrSubmitResponse<CaseData, State> response = caseworkerCloseTheCase.midEvent(caseDetails, caseDetails);

        //Then
        assert (response.getErrors().contains(DOCUMENT_VALIDATION_MESSAGE));
    }

}
