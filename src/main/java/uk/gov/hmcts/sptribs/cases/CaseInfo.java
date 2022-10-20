package uk.gov.hmcts.sptribs.cases;

import lombok.Builder;
import lombok.Getter;
import uk.gov.hmcts.sptribs.cases.model.CaseData;
import uk.gov.hmcts.sptribs.cases.model.State;

import java.util.List;

@Builder
@Getter
public class CaseInfo {

    private final CaseData caseData;
    private final State state;
    private final List<String> errors;
}
