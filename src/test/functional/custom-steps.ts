export = function () {
  return actor({
    loginAs: function (username, password) {
      this.amOnPage('/');
      this.see('Sign in');
      this.fillField('#username', username);
      this.fillField('#password', password);
      this.click('Sign in');
      this.waitForText('Filters', 60);
      this.seeInCurrentUrl('/cases');
    },
    logout: function () {
      this.click('Sign out');
      this.see('Sign in');
    },
    createCase: async function () {
      this.amOnPage('/cases/case-filter');
      this.waitForText('Create Case', 10);
      this.waitForValue('#cc-jurisdiction', 'DIVORCE', 20);
      this.selectOption('Jurisdiction', 'CIC');
      this.selectOption('Case type', 'CIC Case Type');
      this.selectOption('Event', 'Create Case');
      this.click('Start');
      this.selectOption('Case Category', 'Eligibility');
      this.selectOption('CIC Case Subcategory', 'Sexual Abuse');
      this.click('Continue');
      this.appendField('Day','14');
      this.appendField('Month','10');
      this.appendField('Year','2022');
      this.click('Continue');
      this.checkOption('Subject');
      this.click('Continue');
      this.appendField('Subject Full Name', 'David Warner');
      this.appendField('Day','1');
      this.appendField('Month','1');
      this.appendField('Year','1990');
      this.checkOption('#cicCaseContactPreferenceType-Email');
      this.appendField('Subject Email address','Vijay.Kayyam@HMCTS.NET');
      this.click('Continue');
      this.checkOption('#cicCaseSubjectCIC-SubjectCIC');
      this.click('Continue');
      this.click('Continue');
      this.selectOption('Scheme', '2012');
      this.checkOption('#cicCaseClaimLinkedToCic_No');
      this.checkOption('#cicCaseCompensationClaimLinkCIC_No');
      this.checkOption('#cicCaseFormReceivedInTime_No');
      this.checkOption('#cicCaseMissedTheDeadLineCic_No');
      this.click('Continue');
      this.click('Save and continue');
      this.waitForText('Case Created', 20);
      this.see('Case reference number');
      let caseNumber = await this.grabTextFrom('//h2[2]');
      console.log(caseNumber);
      return caseNumber;
    }
  });
};
