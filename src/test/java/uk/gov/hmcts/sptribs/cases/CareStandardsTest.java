package uk.gov.hmcts.sptribs.cases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.sptribs.cases.model.CaseData;
import uk.gov.hmcts.sptribs.cases.model.State;
import uk.gov.hmcts.sptribs.cases.model.UserRole;
import uk.gov.hmcts.sptribs.common.ccd.CcdCaseType;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.sptribs.testutil.ConfigTestUtil.createCaseDataConfigBuilder;

@ExtendWith(MockitoExtension.class)
public class CareStandardsTest {

    @InjectMocks
    private CareStandards careStandards;

    @Test
    void shouldAddSystemUpdateUserAccessToDraftStateWhenEnvironmentIsAat() {
        //Given
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

        //When
        careStandards.configure(configBuilder);

        //Then
        assertThat(configBuilder.build().getCaseType()).isEqualTo(CcdCaseType.CS.name());

    }

}
