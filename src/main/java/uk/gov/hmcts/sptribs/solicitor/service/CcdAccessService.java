package uk.gov.hmcts.sptribs.solicitor.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CaseAssignmentApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseAssignmentUserRole;
import uk.gov.hmcts.reform.ccd.client.model.CaseAssignmentUserRoleWithOrganisation;
import uk.gov.hmcts.reform.ccd.client.model.CaseAssignmentUserRolesRequest;
import uk.gov.hmcts.reform.idam.client.models.User;
import uk.gov.hmcts.sptribs.ciccase.model.UserRole;
import uk.gov.hmcts.sptribs.idam.IdamService;

import java.util.List;
import java.util.stream.Collectors;

import static uk.gov.hmcts.sptribs.ciccase.model.UserRole.CREATOR;

@Service
@Slf4j
public class CcdAccessService {

    @Autowired
    private CaseAssignmentApi caseAssignmentApi;

    @Autowired
    private IdamService idamService;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Retryable(value = {FeignException.class, RuntimeException.class})
    public void addRoleToCase(String userId, Long caseId, String orgId, UserRole role) {
        log.info("Adding roles {} to case Id {} and user Id {}", role, caseId, userId);

        User systemUpdateUser = idamService.retrieveSystemUpdateUserDetails();
        String idamToken = systemUpdateUser.getAuthToken();
        String s2sToken = authTokenGenerator.generate();

        caseAssignmentApi.addCaseUserRoles(
            idamToken,
            s2sToken,
            getCaseAssignmentRequest(caseId, userId, orgId, role)
        );

        log.info("Successfully added the role to case Id {} ", caseId);
    }


    @Retryable(value = {FeignException.class, RuntimeException.class})
    public boolean isApplicant1(String userToken, Long caseId) {
        log.info("Retrieving roles for user on case {}", caseId);
        User user = idamService.retrieveUser(userToken);
        List<String> userRoles =
            caseAssignmentApi.getUserRoles(
                userToken,
                authTokenGenerator.generate(),
                List.of(String.valueOf(caseId)),
                List.of(user.getUserDetails().getId())
            )
            .getCaseAssignmentUserRoles()
            .stream()
            .map(CaseAssignmentUserRole::getCaseRole)
            .collect(Collectors.toList());
        return userRoles.contains(CREATOR.getRole());
    }

    public void removeUsersWithRole(Long caseId, List<String> roles) {
        final var auth = idamService.retrieveSystemUpdateUserDetails().getAuthToken();
        final var s2sToken = authTokenGenerator.generate();
        final var response = caseAssignmentApi.getUserRoles(auth, s2sToken, List.of(caseId.toString()));

        final var assignmentUserRoles = response.getCaseAssignmentUserRoles()
            .stream()
            .filter(caseAssignment -> roles.contains(caseAssignment.getCaseRole()))
            .map(caseAssignment -> getCaseAssignmentUserRole(caseId, null, caseAssignment.getCaseRole(), caseAssignment.getUserId()))
            .collect(Collectors.toList());

        if (!assignmentUserRoles.isEmpty()) {
            final var caseAssignmentUserRolesReq = CaseAssignmentUserRolesRequest.builder()
                .caseAssignmentUserRolesWithOrganisation(assignmentUserRoles)
                .build();

            caseAssignmentApi.removeCaseUserRoles(auth, s2sToken, caseAssignmentUserRolesReq);
        }
    }

    private CaseAssignmentUserRolesRequest getCaseAssignmentRequest(Long caseId, String userId, String orgId, UserRole role) {
        return CaseAssignmentUserRolesRequest.builder()
            .caseAssignmentUserRolesWithOrganisation(
                List.of(getCaseAssignmentUserRole(caseId, orgId, role.getRole(), userId))
            ).build();
    }

    private CaseAssignmentUserRoleWithOrganisation getCaseAssignmentUserRole(Long caseId, String orgId, String role, String userId) {
        return CaseAssignmentUserRoleWithOrganisation.builder()
            .organisationId(orgId)
            .caseDataId(String.valueOf(caseId))
            .caseRole(role)
            .userId(userId)
            .build();
    }
}