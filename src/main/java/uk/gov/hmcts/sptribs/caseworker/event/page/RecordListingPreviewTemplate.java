package uk.gov.hmcts.sptribs.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.sptribs.caseworker.model.RecordListing;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;
import uk.gov.hmcts.sptribs.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.sptribs.common.ccd.PageBuilder;

import java.util.HashMap;
import java.util.Map;

public class RecordListingPreviewTemplate implements CcdPageConfiguration {

    @Override
    public void addTo(PageBuilder pageBuilder) {
        //TODO: Issue with pageShowCondition in recordListingPreviewTemplate. Need to look into the issue - Santoshini
        final String recordListingPreviewTemplateObj = "recordListingPreviewTemplateObj";
        Map<String, String> map = new HashMap<>();
        map.put(recordListingPreviewTemplateObj,"recordHearingNotice = \"Create from a template\"");
        map.put("uploadHearingNoticeObj1","recordHearingNotice = \"Upload from your computer\"");

        pageBuilder.page(recordListingPreviewTemplateObj)
            .label(recordListingPreviewTemplateObj, "<h1>Preview template</h1>")
            .pageShowConditions(map)
            .complex(CaseData::getRecordListing)
            .readonly(RecordListing::getRecordListingDraft)
            .label("recordListingPreviewTemplateInfo","If there are no changes to make, continue to the next screen.")
            .done();
    }
}
