package uk.gov.hmcts.sptribs.document.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.sptribs.caseworker.model.HearingSummary;
import uk.gov.hmcts.sptribs.caseworker.model.Listing;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.ciccase.model.CicCase;
import uk.gov.hmcts.sptribs.ciccase.model.PanelMember;
import uk.gov.hmcts.sptribs.ciccase.model.SchemeCic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static uk.gov.hmcts.sptribs.testutil.TestConstants.TEST_CASE_ID;

@ExtendWith(MockitoExtension.class)
public class DecisionTemplateContentTest {

    @InjectMocks
    private DecisionTemplateContent templateContent;

    @Test
    public void shouldSuccessfullyApplyDecisionContent() {
        //Given
        CaseData caseData = buildCaseData();
        caseData.setDecisionSignature("John Doe");
        caseData.setDecisionMainContent("Case Closed");
        Listing listing = Listing.builder().date(LocalDate.now()).hearingTime("11::00").build();
        caseData.setListing(listing);
        HearingSummary summary = HearingSummary.builder()
            .memberList(getMembers())
            .build();
        caseData.getListing().setSummary(summary);
        //When
        Map<String, Object> result = templateContent.apply(caseData, TEST_CASE_ID);

        //Then
        assertThat(result).contains(
            entry("cicCaseSchemeCic", SchemeCic.Year1996.getLabel())
        );
    }

    @Test
    public void shouldSuccessfullyApplyDecisionContentNoMembers() {
        //Given
        CaseData caseData = buildCaseData();
        HearingSummary summary = HearingSummary.builder()
            .build();
        Listing listing = Listing.builder()
            .summary(summary)
            .date(LocalDate.now())
            .hearingTime("11::00")
            .build();

        caseData.setListing(listing);
        //When
        Map<String, Object> result = templateContent.apply(caseData, TEST_CASE_ID);

        //Then
        assertThat(result).contains(
            entry("cicCaseSchemeCic", SchemeCic.Year1996.getLabel())
        );
    }

    private CaseData buildCaseData() {
        final CicCase cicCase = CicCase.builder().schemeCic(SchemeCic.Year1996).build();

        return CaseData.builder()
            .cicCase(cicCase)
            .build();
    }

    private List<ListValue<PanelMember>> getMembers() {
        List<ListValue<PanelMember>> members = new ArrayList<>();
        ListValue<PanelMember> member = new ListValue<>();
        PanelMember panelMember1 = PanelMember.builder()
            .name(getDynamicList())
            .build();
        member.setValue(panelMember1);
        members.add(member);
        return members;
    }

    private DynamicList getDynamicList() {
        final DynamicListElement listItem = DynamicListElement
            .builder()
            .label("Jane Doe")
            .code(UUID.randomUUID())
            .build();
        return DynamicList
            .builder()
            .value(listItem)
            .listItems(List.of(listItem))
            .build();
    }
}
