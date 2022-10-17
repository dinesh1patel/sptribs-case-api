import {config as testConfig} from '../config';

export const config: CodeceptJS.Config = {
  tests: './*-test.ts',
  output: '../../../functional-output/functional/reports',
  helpers: testConfig.helpers,
  timeout: testConfig.WaitForTimeout,
  include: {
    I: './custom-steps.ts',
  },
  async teardownAll() {
  },
  mocha: {},
  name: 'functional',
  plugins: {
    pauseOnFail: {},
    retryFailedStep: {
      enabled: true,
    },
    allure: {
      enabled: true,
    },
    tryTo: {
      enabled: true,
    },
    screenshotOnFail: {
      enabled: true,
    }
  },
};
