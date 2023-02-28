package uk.gov.hmcts.sptribs.e2e;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.sptribs.testutils.DateHelpers;

import java.util.Calendar;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static uk.gov.hmcts.sptribs.testutils.AssertionHelpers.textOptionsWithTimeout;
import static uk.gov.hmcts.sptribs.testutils.PageHelpers.clickButton;
import static uk.gov.hmcts.sptribs.testutils.PageHelpers.getTextBoxByLabel;


public class CloseCase extends Base {
    @Test
    @Order(1)
    public void caseworkerShouldAbleToClosetheCase() {
        Login login = new Login();
        login.loginAsStTest1User();
        Case newCase = new Case();
        newCase.createCase();
        newCase.buildCase();
        newCase.startNextStepAction("Case: Close case");
        assertThat(page.locator("h1")).hasText("Are you sure you want to close this case?", textOptionsWithTimeout(30000));
        clickButton("Continue");
        page.getByLabel("Case Withdrawn").check();
        clickButton("Continue");
        assertThat(page.locator("h1")).hasText("Withdrawal details", textOptionsWithTimeout(30000));
        page.getByLabel("Who withdrew from the case?").fill("case worker");
        Calendar date = DateHelpers.getYesterdaysDate();
        getTextBoxByLabel("Day").fill(String.valueOf(date.get(Calendar.DAY_OF_MONTH)));
        getTextBoxByLabel("Month").fill(String.valueOf(date.get(Calendar.MONTH) + 1));
        getTextBoxByLabel("Year").type(String.valueOf(date.get(Calendar.YEAR)));
        page.locator("h1").click();
        clickButton("Continue");
        assertThat(page.locator("h1")).hasText("Upload case documents", textOptionsWithTimeout(30000));
        clickButton("Continue");
        assertThat(page.locator("h1")).hasText("Select recipients", textOptionsWithTimeout(30000));
        page.getByLabel("Subject").check();
        page.getByLabel("Respondent").check();
        clickButton("Continue");
        assertThat(page.locator("h2")).hasText("Check your answers", textOptionsWithTimeout(30000));
        clickButton("Save and continue");
        assertThat(page.locator("ccd-markdown markdown h1"))
            .hasText("Case closed", textOptionsWithTimeout(60000));
        clickButton("Close and Return to case details");
        assertThat(page.locator("h2.heading-h2").first())
            .hasText("History", textOptionsWithTimeout(60000));
        Assertions.assertEquals("Case closed", newCase.getCaseStatus());
    }

    @Test
    public void caseworkerShouldAbleToReinstantCase() {
        Login login = new Login();
        login.loginAsStTest1User();
        Case newCase = new Case();
        newCase.createCase();
        newCase.buildCase();
        newCase.caseworkerShouldAbleToClosetheCase();
        newCase.startNextStepAction("Case: Reinstate case");
        assertThat(page.locator("h1")).hasText("Are you sure you want to reinstate this case?", textOptionsWithTimeout(30000));
        clickButton("Continue");
        assertThat(page.locator("h1")).hasText("Reason for reinstatement", textOptionsWithTimeout(30000));
        page.getByLabel("Request following a withdrawal decision").check();
        clickButton("Continue");
        assertThat(page.locator("h1")).hasText("Upload documents", textOptionsWithTimeout(30000));
        clickButton("Continue");
        assertThat(page.locator("h1")).hasText("Contact parties", textOptionsWithTimeout(30000));
        clickButton("Continue");
        assertThat(page.locator("h2")).hasText("Check your answers", textOptionsWithTimeout(30000));
        clickButton("Save and continue");
        assertThat(page.locator("ccd-markdown markdown h1"))
            .hasText("Case reinstated", textOptionsWithTimeout(60000));
        clickButton("Close and Return to case details");
        assertThat(page.locator("h2.heading-h2").first())
            .hasText("History", textOptionsWithTimeout(60000));
        Assertions.assertEquals("Case management", newCase.getCaseStatus());
    }
}



