Feature('Feature 1');

Before(async ({ I }) => {
  I.loginAs('TEST_SOLICITOR@mailinator.com', 'genericPassword123');
  I.waitForText('Case list');
});

Scenario('Create Case',
  async ({ I }) => {
    await I.createCase();
    I.see('Case reference number');
  }
).tag('@CrossBrowser');

Scenario('Create flag at case level',
  async ({ I }) => {
    let caseNumber = await I.createCase();
    let dehyphenatedCaseNumber = caseNumber.replace(/-/g, '');
    I.amOnPage('/cases/case-details/' + dehyphenatedCaseNumber +'#History');
    I.see(caseNumber, 'h3');
    I.see('History', 'h2');
    I.selectOption('#next-step','Create a case flag');
    I.click('Go');
    I.checkOption('#caseFlagFlagLevel-CaseLevel');
    I.click('Continue');
    I.checkOption('#caseFlagFlagType-Other');
    I.appendField('Enter a flag type', 'This is a flag type');
    I.click('Continue');
    I.click('Continue');
    I.click('Save and continue');
    I.see('Case Flag created');
    I.see('This Flag has been added to case');
  }
).tag('@CrossBrowser');

Scenario('Create Record Listing',
  async ({ I }) => {
    let caseNumber = await I.createCase();
    I.see('Case Created', 'ccd-markdown h1');
    I.click('Close and Return to case details');
    I.see(caseNumber, 'h3');
    I.see('History', 'h2');
    I.selectOption('#next-step','Case built');
    I.click('Go');
    I.click('Save and continue');
    I.see('Case built successful');
    I.click('Close and Return to case details');
    I.selectOption('#next-step','Record listing');
    I.click('Go');
    I.see('Hearing type and format', 'ccd-markdown h1');
    I.checkOption('#recordHearingType-JudicialMediation');
    I.checkOption('#recordHearingFormat-Video');
    I.click('Continue');
    I.see('Remote hearing information', 'ccd-markdown h1');
    I.click('Continue');
    I.see('Other information', 'ccd-markdown h1');
    I.click('Continue');
    I.see('Check your answers', 'form.check-your-answers h2');
    I.click('Save and continue');
    I.see('Listing record created');
  }
).tag('@CrossBrowser');