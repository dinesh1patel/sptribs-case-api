import config from '../config'

Feature('Stay case');

let caseNumber: string;
let dehyphenatedCaseNumber: string;

Before( async ({ I }) => {
    await I.amOnPage('/');
});

Scenario('I should be able to add stay @test', async ({ I }) => {
    caseNumber = await I.createCase();
    dehyphenatedCaseNumber = caseNumber.replace(/-/g, '');
    await I.amOnPage(`/cases/case-details/${dehyphenatedCaseNumber}#History`);
    await I.see(caseNumber, 'h3');
    await I.see('History', 'h2');
});

Scenario('I should be able to edit stay @test', async ({ I }) => {
    await I.login(config.username);
    await I.amOnPage(`/cases/case-details/${dehyphenatedCaseNumber}#History`);
    await I.see(caseNumber, 'h3');
    await I.see('History', 'h2');
});