package uk.gov.hmcts.sptribs.cases.model;

public enum LanguagePreference {
    ENGLISH("english"),
    WELSH("welsh");

    private final String code;

    LanguagePreference(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}