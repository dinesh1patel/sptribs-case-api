package uk.gov.hmcts.sptribs.cases.model.access;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import uk.gov.hmcts.ccd.sdk.api.HasAccessControl;
import uk.gov.hmcts.ccd.sdk.api.HasRole;
import uk.gov.hmcts.ccd.sdk.api.Permission;

import static uk.gov.hmcts.sptribs.cases.model.UserRole.CITIZEN_CIC;
import static uk.gov.hmcts.sptribs.cases.model.UserRole.COURT_ADMIN_CIC;
import static uk.gov.hmcts.sptribs.cases.model.UserRole.CREATOR;
import static uk.gov.hmcts.sptribs.cases.model.UserRole.SYSTEMUPDATE;

public class Applicant2Access implements HasAccessControl {
    @Override
    public SetMultimap<HasRole, Permission> getGrants() {
        SetMultimap<HasRole, Permission> grants = HashMultimap.create();
        grants.putAll(COURT_ADMIN_CIC, Permissions.READ);
        grants.putAll(CITIZEN_CIC, Permissions.READ);
        grants.putAll(CREATOR, Permissions.READ);
        grants.putAll(SYSTEMUPDATE, Permissions.CREATE_READ_UPDATE);
        return grants;
    }
}