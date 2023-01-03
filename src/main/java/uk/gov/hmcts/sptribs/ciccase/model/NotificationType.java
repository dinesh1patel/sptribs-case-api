package uk.gov.hmcts.sptribs.ciccase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.ccd.sdk.api.HasLabel;

@Getter
@AllArgsConstructor
public enum NotificationType implements HasLabel {

    @JsonProperty("Email")
    EMAIL("Email"),

    @JsonProperty("Post")
    POST("Post");

    private final String label;

}