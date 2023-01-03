package uk.gov.hmcts.sptribs.caseworker.event.page;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.sptribs.caseworker.model.CaseIssueFinalDecision;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.ciccase.model.CicCase;
import uk.gov.hmcts.sptribs.ciccase.model.FinalDecisionRecipientRepresentativeCIC;
import uk.gov.hmcts.sptribs.ciccase.model.FinalDecisionRecipientSubjectCIC;
import uk.gov.hmcts.sptribs.ciccase.model.RepresentativeCIC;
import uk.gov.hmcts.sptribs.ciccase.model.State;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class IssueFinalDecisionSelectRecipientsTest {

    @InjectMocks
    private IssueFinalDecisionSelectRecipients selectRecipients;

    @Test
    void shouldBeSuccessfulForValidRecipients() {
        //Given
        final CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        final Set<FinalDecisionRecipientSubjectCIC> subjectCICSet = new HashSet<>();
        subjectCICSet.add(FinalDecisionRecipientSubjectCIC.SUBJECT);
        final CaseIssueFinalDecision finalDecision = CaseIssueFinalDecision.builder().recipientSubjectCIC(subjectCICSet).build();

        final CaseData caseData = CaseData.builder()
            .caseIssueFinalDecision(finalDecision)
            .build();
        caseDetails.setData(caseData);

        //When
        final AboutToStartOrSubmitResponse<CaseData, State> response = selectRecipients.midEvent(caseDetails, caseDetails);

        //Then
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void shouldBeInvalidIfNoRecipientsSelected() {
        //Given
        final CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        final CaseIssueFinalDecision finalDecision = CaseIssueFinalDecision.builder().recipientSubjectCIC(new HashSet<>()).build();

        final CaseData caseData = CaseData.builder()
            .caseIssueFinalDecision(finalDecision)
            .build();
        caseDetails.setData(caseData);

        //When
        final AboutToStartOrSubmitResponse<CaseData, State> response = selectRecipients.midEvent(caseDetails, caseDetails);

        //Then
        assertThat(response.getErrors()).hasSize(1);
    }

    @Test
    void shouldBeValidIfRepresentativeSelectedWithRepresentativeDetails() {
        //Given
        final CaseDetails<CaseData, State> caseDetails = new CaseDetails<>();
        final Set<FinalDecisionRecipientRepresentativeCIC> decisionRepresentativeCICSet = new HashSet<>();
        decisionRepresentativeCICSet.add(FinalDecisionRecipientRepresentativeCIC.REPRESENTATIVE);
        final Set<RepresentativeCIC> representativeCICSet = new HashSet<>();
        representativeCICSet.add(RepresentativeCIC.REPRESENTATIVE);
        final CaseIssueFinalDecision finalDecision =
            CaseIssueFinalDecision.builder().recipientRepresentativeCIC(decisionRepresentativeCICSet).build();
        final CicCase cicCase = CicCase.builder().representativeCIC(representativeCICSet).build();

        final CaseData caseData = CaseData.builder()
            .caseIssueFinalDecision(finalDecision)
            .cicCase(cicCase)
            .build();
        caseDetails.setData(caseData);

        //When
        final AboutToStartOrSubmitResponse<CaseData, State> response = selectRecipients.midEvent(caseDetails, caseDetails);

        //Then
        assertThat(response.getErrors()).isEmpty();
    }
}