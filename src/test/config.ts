export const config = {
  TEST_URL: 'http://localhost:3000',
  TestHeadlessBrowser: process.env.TEST_HEADLESS ? process.env.TEST_HEADLESS === 'true' : false,
  WaitForTimeout: 60000,
  Gherkin: {
    features: './features/**/*.feature',
    steps: './steps/**/*.ts',
  },
  helpers: {},
};

config.helpers = {
  Playwright: {
    url: config.TEST_URL,
    show: config.TestHeadlessBrowser,
    browser: 'chromium',
    // browser: 'firefox',
    // browser: 'webkit',
    waitForTimeout: config.WaitForTimeout,
    waitForAction: 1000,
    waitForNavigation: 'networkidle0',
    ignoreHTTPSErrors: true,
  }
};

