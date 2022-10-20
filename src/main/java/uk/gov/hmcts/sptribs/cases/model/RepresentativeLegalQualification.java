package uk.gov.hmcts.sptribs.cases.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum RepresentativeLegalQualification implements HasLabel {



    @JsonProperty("Qualified")
    EMAIL("Yes", "Yes"),
    POST("No", "No");
    private String type;
    private final String label;
}