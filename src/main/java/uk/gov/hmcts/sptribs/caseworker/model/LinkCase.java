package uk.gov.hmcts.sptribs.caseworker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.CaseLink;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.ScannedDocument;
import uk.gov.hmcts.sptribs.ciccase.model.access.CaseworkerWithCAAAccess;
import uk.gov.hmcts.sptribs.ciccase.model.access.DefaultAccess;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.Collection;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.ComponentLauncher;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkCase {

    @CCD(
        label = "Collection of Case Links",
        typeOverride = Collection,
        typeParameterOverride = "CaseLink",
        access = {DefaultAccess.class, CaseworkerWithCAAAccess.class}
    )
    private List<ListValue<CaseLink>> caseLinks;

    @CCD(
        label = "Launch the web component screen",
        typeOverride = ComponentLauncher,
        access = {DefaultAccess.class, CaseworkerWithCAAAccess.class}
    )
    private uk.gov.hmcts.ccd.sdk.type.ComponentLauncher componentLauncher;
}
