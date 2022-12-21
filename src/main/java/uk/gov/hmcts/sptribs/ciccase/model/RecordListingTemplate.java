package uk.gov.hmcts.sptribs.ciccase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecordListingTemplate {
    @JsonProperty("Hearing invite - CVP")
    HEARING_INVITE_CVP("CIC6_General_Directions", "Hearing invite - CVP"),

    @JsonProperty("Hearing invite - F2F")
    HEARING_INVITE_F2F("ST-CIC-HNO-ENG-hearing_invite_F2F", "Hearing invite - F2F"),

    @JsonProperty("Hearing invite - telephone")
    HEARING_INVITE_TELEPHONE("ST-CIC-HNO-ENG-hearing_invite_telephone", "Hearing invite - telephone");

    private final String name;
    private final String label;
}
