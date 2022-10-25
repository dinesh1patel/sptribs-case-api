package uk.gov.hmcts.sptribs.ciccase.validation;

import uk.gov.hmcts.sptribs.ciccase.model.Application;
import uk.gov.hmcts.sptribs.ciccase.model.CaseData;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.sptribs.ciccase.validation.ValidationUtil.flattenLists;
import static uk.gov.hmcts.sptribs.ciccase.validation.ValidationUtil.validateApplicant2BasicCase;
import static uk.gov.hmcts.sptribs.ciccase.validation.ValidationUtil.validateBasicCase;
import static uk.gov.hmcts.sptribs.ciccase.validation.ValidationUtil.validateCaseFieldsForIssueApplication;

public final class ApplicationValidation {

    private ApplicationValidation() {

    }

    public static List<String> validateReadyForPayment(CaseData caseData) {
        List<String> errors = validateBasicCase(caseData);

        if (caseData.getApplicationType() != null && !caseData.getApplicationType().isSole()) {
            errors.addAll(validateApplicant2BasicCase(caseData));
        }

        return errors;
    }

    public static List<String> validateSubmission(Application application) {
        List<String> errors = new ArrayList<>();

        if (!application.hasBeenPaidFor()) {
            errors.add("Payment incomplete");
        }

        if (!application.applicant1HasStatementOfTruth() && !application.hasSolSignStatementOfTruth()) {
            errors.add("Statement of truth must be accepted by the person making the application");
        }

        return errors;
    }

    public static List<String> validateIssue(CaseData caseData) {
        return flattenLists(
            validateBasicCase(caseData),
            validateCaseFieldsForIssueApplication(caseData.getApplication().getMarriageDetails())
        );
    }

}