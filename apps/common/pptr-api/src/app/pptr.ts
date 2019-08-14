const express = require('express');
const bodyParser = require('body-parser');
const puppeteer = require('puppeteer');
const locateChrome = require('locate-chrome');

const PORT = 3000;

let chromePath;
locateChrome(path => chromePath = path);

const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/healthz', async(req, res) => {
  res.status(200);
  res.send('');
});

app.post('/pptr/crawl', async (req, res) => {
  if (req.body.url) {
    const result = await buildAndSendResponse(res, req.body.url, req.body.options);

    res.contentType('application/json');
    res.send(result);
  } else {
    res.send('no url specified in the body of this post request');
  }
});

async function buildAndSendResponse(res, url, options: any = {}) {
  const browser = await puppeteer.launch({ args: ['--no-sandbox'], executablePath: chromePath, ignoreHTTPSErrors: true });
  const page = await browser.newPage();

  page.on('console', msg => {
    if (msg.args().length > 0) {
      console.log(new Date().toJSON() + ' headless console: ');
      for (let i = 0; i < msg.args().length; ++i) {
        console.log(`${i}: ${msg.args()[i]}`);
      }
    }
  });

  //await page.emulateMedia('print');

  await page.evaluateOnNewDocument((msg) => {
    console.log(msg);
  }, "Hello puppeteer!");

  try {
    await page.goto(url, {
      waitUntil: 'load'
    });

    if (options.waitForSelector) {
      await page.waitForSelector(options.waitForSelector);
    }

    const result = await page.evaluate(() => document.querySelector('html').innerHTML);

    console.log(result);

    //const pdfBuffer = await page.pdf(Object.assign({}, options.pdfOptions));

    await browser.close();

    return { result };
  } catch(error) {
    await browser.close();

    console.error(error);
  }

}

app.listen(PORT, () => console.log(`pdf-server listening on port ${PORT}`));
