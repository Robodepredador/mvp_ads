const { chromium } = require('@playwright/test');

(async () => {
  const browser = await chromium.launch();
  const page = await browser.newPage();

  page.on('console', (msg) => {
    console.log(`BROWSER:${msg.type()}: ${msg.text()}`);
  });
  page.on('pageerror', (err) => {
    console.log(`BROWSER:pageerror: ${err.message}`);
  });
  page.on('requestfailed', (request) => {
    console.log(`REQUEST_FAILED ${request.url()} - ${request.failure().errorText}`);
  });

  await page.goto('http://localhost:4200/login', { waitUntil: 'domcontentloaded' });
  await page.fill('input[name="username"]', 'programacion');
  await page.fill('input[name="password"]', 'mvp2024');
  await page.click('button.login-button');
  await page.waitForTimeout(8000);
  console.log('FINAL_URL', page.url());
  const header = await page.textContent('h1').catch(() => null);
  console.log('HEADER', header);
  const html = await page.content();
  require('fs').writeFileSync('playwright-login.html', html);
  await browser.close();
})();

