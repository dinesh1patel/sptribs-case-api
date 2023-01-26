package uk.gov.hmcts.sptribs.ciccase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.ccd.sdk.api.CCD;
import uk.gov.hmcts.ccd.sdk.type.AddressGlobalUK;

import static uk.gov.hmcts.ccd.sdk.type.FieldType.FixedList;
import static uk.gov.hmcts.ccd.sdk.type.FieldType.TextArea;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralLetter {

    @CCD(
        label = "Address to",
        typeOverride = FixedList,
        typeParameterOverride = "GeneralParties"
    )
    private GeneralParties generalLetterParties;

    @CCD(label = "Recipient's name")
    private String otherRecipientName;

    @CCD(label = "Recipient's address")
    private AddressGlobalUK otherRecipientAddress;

    @CCD(
        label = "Please provide details",
        typeOverride = TextArea
    )
    private String generalLetterDetails;

}
