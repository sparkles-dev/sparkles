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
    crawlUrl(req.body.url, req.body.options)
      .then(result => {
        res.contentType('application/json');
        res.send(result);
      })
      .catch(err => {
        res.status(205);
        res.contentType('application/json');
        res.send(err);
      });
  } else {
    res.send('no url specified in the body of this post request');
  }
});

/**
 * Opens a chrome headless instance, instruments the browser to wait for a given selector and return its result.
 *
 * @param url
 * @param options
 */
async function crawlUrl(url, options: any = {}) {
  // Override user agent to hide headless chrome
  // https://medium.com/@jsoverson/how-to-bypass-access-denied-pages-with-headless-chrome-87ddd5f3413c
  const userAgent = process.env.CHROME_USER_AGENT || 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36';
  const browser = await puppeteer.launch({
    args: ['--no-sandbox', `--user-agent ${userAgent}`],
    executablePath: chromePath,
    ignoreHTTPSErrors: true
  });

  // Set up pptr
  const page = await browser.newPage();
  page.on('console', msg => {
    if (msg.args().length > 0) {
      console.log(new Date().toJSON() + ' headless console: ');
      for (let i = 0; i < msg.args().length; ++i) {
        console.log(`${i}: ${msg.args()[i]}`);
      }
    }
  });

  let response;
  try {
    // Load target web page
    response = await page.goto(url, {
      waitUntil: 'load'
    });

    if (options.waitForSelector) {
      await page.waitForSelector(options.waitForSelector);
    }

    const querySelector = options.querySelector || 'html';
    const result = await page.evaluate((selector) => document.querySelector(selector).innerHTML, querySelector);

    // console.log(result);
    // await page.emulateMedia('print');
    // const pdfBuffer = await page.pdf(Object.assign({}, options.pdfOptions));

    return { result };
  } catch(err) {
    console.error(err);
    const screenshot = await page.screenshot({ encoding: 'base64' });

    throw {
      error: err,
      screenshot,
      response: {
        status: response.status(),
        statusText: response.statusText(),
        url: response.url(),
        headers: response.headers()
      }
    };
  } finally {
    await browser.close();
  }
}

app.listen(PORT, () => console.log(`pptr-api listening on port ${PORT}`));
