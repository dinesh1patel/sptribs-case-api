package uk.gov.hmcts.sptribs.ciccase.model.categories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.ConfigBuilder;
import uk.gov.hmcts.sptribs.ciccase.ConfigBuilderHelper;
import uk.gov.hmcts.sptribs.ciccase.PrimaryHealthLists;
import uk.gov.hmcts.sptribs.ciccase.model.State;
import uk.gov.hmcts.sptribs.ciccase.model.UserRole;
import uk.gov.hmcts.sptribs.ciccase.model.casetype.PrimaryHealthListsData;

@Component
@Slf4j
public class CategoriesPHL extends PrimaryHealthLists {

    @Override
    public void configure(final ConfigBuilder<PrimaryHealthListsData, State, UserRole> configBuilder) {
        ConfigBuilderHelper.configureWithMandatoryConfig(configBuilder);
        ConfigBuilderHelper.configureCategories(configBuilder);
    }
}
