import { Link } from './resources';

export interface UriParams {
  [key: string]: any
}

export function expand(link: Link, params: UriParams): string {
  let url: string;
  if (link.templated) {
    // TODO: expand uri template
    // https://github.com/bramstein/url-template/blob/master/lib/url-template.js
    // https://github.com/geraintluff/uri-templates
    url = link.href;
    Object.keys(params).forEach(key => {
      url = url.replace(`{${key}}`, params[key]);
    });
  } else {
    url = link.href;
  }

  return url;
}
