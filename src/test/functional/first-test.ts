Feature('Feature 1');

Before(async ({I}) => {
  I.loginAs('TEST_SOLICITOR@mailinator.com', 'genericPassword123');
  I.waitForText('Case list');
});

Scenario('Create a case',
  async ({I}) => {
    let pageHeader;
    do {
      I.click('Create case');
      pageHeader = await I.grabTextFrom('//h1');
    } while (pageHeader !== ' Create Case ');
    I.waitForText('Create Case', 10);
    I.waitForValue('#cc-jurisdiction', 'DIVORCE', 20);
    I.selectOption('Jurisdiction', 'CIC');
    I.selectOption('Case type', 'CIC Case Type');
    I.selectOption('Event', 'Create Case');
    I.click('Start');
    I.selectOption('Case Category', 'Eligibility');
    I.selectOption('CIC Case Subcategory', 'Sexual Abuse');
    I.click('Continue');
    I.appendField('Day', '14');
    I.appendField('Month', '10');
    I.appendField('Year', '2022');
    I.click('Continue');
    I.checkOption('Subject');
    I.click('Continue');
    I.appendField('Subject Full Name', 'David Warner');
    I.appendField('Day', '1');
    I.appendField('Month', '1');
    I.appendField('Year', '1990');
    I.checkOption('#cicCaseContactPreferenceType-Email');
    // I.checkOption('What is subject contact preference type?', 'Email');
    I.appendField('Subject Email address', 'Vijay.Kayyam@HMCTS.NET');
    I.click('Continue');
    I.checkOption('#cicCaseSubjectCIC-SubjectCIC');
    I.click('Continue');
    I.click('Continue');
    I.selectOption('Scheme', '2012');
    I.checkOption('#cicCaseClaimLinkedToCic_No');
    I.checkOption('#cicCaseCompensationClaimLinkCIC_No');
    I.checkOption('#cicCaseFormReceivedInTime_No');
    I.checkOption('#cicCaseMissedTheDeadLineCic_No');
    I.click('Continue');
    I.click('Save and continue');
    I.waitForText('Case Created', 20);
    I.see('Case reference number');
    const text = await I.grabTextFrom({css: '.markdown'});
    const caseNumber = text.split(':\n')[1];
    // const caseNumber = text.split(':\n')[1].replace(/-/g,'');
    I.click('Manage Cases');
    I.waitForText('Case list', 10);
    I.click(caseNumber);

    // I.amOnPage('/cases/case-details/' + caseNumber +'#History');
  }
).tag('@CrossBrowser');

Scenario.only('Create a case 2',
  async ({I}) => {
    I.createCase();
    const caseNumber = I.grabTextFrom({css: '.markdown'}).toString().split(':\n')[1]
    I.click('Manage Cases');
    I.waitForText('Case list', 10);
    // I.click(caseNumber);
    // const caseNumber = '1665-9562-2767-3552'.replace(/-/g,'');
    I.amOnPage('/cases/case-details/' + caseNumber +'#History');
    I.selectOption('#next-step','Create a case flag');
    I.click('Go');
    I.checkOption('#caseFlagFlagLevel-CaseLevel');
    I.click('Continue');
    I.checkOption('#caseFlagFlagType-Other');
    I.appendField('Enter a flag type', 'This is a flag type');
    I.click('Continue');
    I.click('Continue');
    I.click('Save and continue');
  });

// After(async ({I}) => {
//   I.logout();
// });
