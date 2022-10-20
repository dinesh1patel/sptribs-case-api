package uk.gov.hmcts.sptribs.cases.tab;

import uk.gov.hmcts.sptribs.cases.model.State;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public final class TabShowCondition {

    private TabShowCondition() {
    }

    public static String showForState(final State... states) {
        return Stream.of(states)
            .map(State:: getName)
            .collect(joining("\" OR [STATE]=\"", "[STATE]=\"", "\""));
    }
}
