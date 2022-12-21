package uk.gov.hmcts.sptribs.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.Document;
import uk.gov.hmcts.sptribs.caseworker.model.RecordListing;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.ciccase.model.LanguagePreference;
import uk.gov.hmcts.sptribs.ciccase.model.State;
import uk.gov.hmcts.sptribs.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.sptribs.common.ccd.PageBuilder;
import uk.gov.hmcts.sptribs.document.CaseDataDocumentService;
import uk.gov.hmcts.sptribs.document.content.RecordlistingTemplateContent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static uk.gov.hmcts.sptribs.document.DocumentConstants.FINAL_DECISION_FILE;

@Slf4j
@Component
public class SelectTemplate implements CcdPageConfiguration {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //TODO: Below attributes are null when we autowire classes to CcdPageConfiguration. Need to look into the issue - Santoshini
    @Autowired
    private CaseDataDocumentService caseDataDocumentService;

    @Autowired
    private RecordlistingTemplateContent recordlistingTemplateContent;

    @Override
    public void addTo(PageBuilder pageBuilder) {
        final String selectTemplateObj = "selectTemplateObj";
        Map<String, String> map = new HashMap<>();
        map.put(selectTemplateObj,"recordHearingNotice = \"Create from a template\"");
        map.put("uploadHearingNoticeObj1","recordHearingNotice = \"Upload from your computer\"");

        pageBuilder.page(selectTemplateObj, this::midEvent)
            .label(selectTemplateObj, "<h1>Select a template</h1>")
            .pageShowConditions(map)
            .complex(CaseData::getRecordListing)
            .mandatory(RecordListing::getTemplate)
            .done();
    }

    public AboutToStartOrSubmitResponse<CaseData, State> midEvent(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> detailsBefore
    ) {

        CaseData caseData = details.getData();
        var recordListing = caseData.getRecordListing();

        final Long caseId = details.getId();

        final String filename = FINAL_DECISION_FILE + LocalDateTime.now().format(formatter);

        Document generalOrderDocument = caseDataDocumentService.renderDocument(
            recordlistingTemplateContent.apply(caseData, caseId),
            caseId,
            recordListing.getTemplate().getName(),
            LanguagePreference.ENGLISH,
            filename
        );

        recordListing.setRecordListingDraft(generalOrderDocument);
        caseData.setRecordListing(recordListing);

        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }
}
