import { ParsedUrl } from '../reframed.interfaces';

const URL_SCHEME = 'app://';

const QUERY_PARAM_RE = /^[^=?&#]+/;

// Return the name of the query param at the start of the string or an empty string
function matchQueryParamName(str: string): string {
  const match = str.match(QUERY_PARAM_RE);
  return match ? match[0] : '';
}

const QUERY_PARAM_VALUE_RE = /^[^?&#]+/;

// Return the value of the query param at the start of the string or an empty string
function matchQueryParamValue(str: string): string {
  const match = str.match(QUERY_PARAM_VALUE_RE);
  return match ? match[0] : '';
}

// Query keys/values should have the "+" replaced first, as "+" in a query string is " ".
// decodeURIComponent function will not decode "+" as a space.
function decodeQuery(s: string): string {
  return decodeURIComponent(s.replace(/\+/g, '%20'));
}

export class UrlParser {
  private remaining: string;

  constructor(url: string, private urlScheme: string) {
    this.remaining = url;
  }

  parse(): ParsedUrl {
    const url = '' + this.remaining;
    if (!this.consumeOptional(this.urlScheme)) {
      throw new Error(`Url must start with ${this.urlScheme}, given ${this.remaining}`);
    }

    const appName = this.captureOptional('/');
    const entryPoint = this.captureOptional('?');
    const params = {};

    do {
      const key = matchQueryParamName(this.remaining);
      if (!key) {
        break;
      }
      this.consumeOptional(key);

      let value: any = '';
      if (this.consumeOptional('=')) {
        value = matchQueryParamValue(this.remaining);
        if (value) {
          this.consumeOptional(value);
        } else {
          value = '';
        }
      }

      const decodedKey = decodeQuery(key);
      const decodedVal = decodeQuery(value);
      if (params.hasOwnProperty(decodedKey)) {
        // Append to existing values
        let currentVal = params[decodedKey];
        if (!Array.isArray(currentVal)) {
          currentVal = [currentVal];
          params[decodedKey] = currentVal;
        }
        currentVal.push(decodedVal);
      } else {
        // Create a new value
        params[decodedKey] = decodedVal;
      }
    } while (this.consumeOptional('&'));

    return {
      url,
      appName,
      entryPoint,
      params
    };
  }

  private peekStartsWith(str: string): boolean {
    return this.remaining.startsWith(str);
  }

  // Consumes the prefix when it is present and returns whether it has been consumed
  private consumeOptional(str: string): boolean {
    if (this.peekStartsWith(str)) {
      this.remaining = this.remaining.substring(str.length);

      return true;
    }

    return false;
  }

  private captureOptional(str: string): string {
    const idx = this.remaining.indexOf(str);

    let value: string;
    if (idx >= 0) {
      value = this.remaining.substr(0, idx);
      this.remaining = this.remaining.substr(idx + 1);
    } else {
      value = this.remaining.substr(0);
      this.remaining = '';
    }

    return value;
  }
}

export function deserializeUrl(url: string, urlScheme = URL_SCHEME): ParsedUrl {
  return new UrlParser(url, urlScheme).parse();
}

export function serializeUrl(url: ParsedUrl, urlScheme = URL_SCHEME) {
  let value = `${urlScheme}/${url.appName}/${url.entryPoint}`;

  if (url.params) {
    const queryString = Object.keys(url.params)
      .map(queryParamKey => {
        const queryParamValue = url.params[queryParamKey];

        return `${encodeURIComponent(queryParamKey)}=${encodeURIComponent(queryParamValue)}`;
      })
      .join('&');

    value = value + '?' + queryString;
  }

  return value;
}
