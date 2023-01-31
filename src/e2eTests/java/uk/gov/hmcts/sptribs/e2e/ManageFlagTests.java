package uk.gov.hmcts.sptribs.e2e;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static com.microsoft.playwright.options.AriaRole.HEADING;
import static uk.gov.hmcts.sptribs.e2e.Actions.CreateFlag;

public class ManageFlagTests extends Base {

    @Test
    public void caseworkerShouldBeAbleToAddFlagAtCaseLevel() {
        new Login().loginAsStTest1User();
        page.selectOption("#next-step", new SelectOption().setLabel(CreateFlag.label));
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Go")).click();
        page.getByLabel("Case", new Page.GetByLabelOptions().setExact(true)).check();
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        page.getByLabel("Subject", new Page.GetByLabelOptions().setExact(true)).check();
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        page.getByLabel("Other").check();
        page.getByLabel("Enter a flag type").click();
        page.getByLabel("Enter a flag type").fill("flag type");
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        page.getByLabel("Explain why you are creating this flag.\nDo not include any sensitive information such as personal details. (Optional)").click();
        page.getByLabel("Explain why you are creating this flag.\nDo not include any sensitive information such as personal details. (Optional)").fill("This is optional comments");
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Save and continue")).click();
        assertThat(page.locator(".markdown h1")).containsText("Case Flag created");
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Close and Return to case details")).click();
        assertThat(page.locator(".alert-message")).containsText("has been updated with event: Flags: Create flag");
    }
}
