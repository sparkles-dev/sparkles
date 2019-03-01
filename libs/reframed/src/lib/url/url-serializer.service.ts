import { Injectable, Inject } from '@angular/core';
import { ParsedUrl, REFRAMED_OPTIONS, ReframedOptions } from '../reframed.interfaces';
import { deserializeUrl, serializeUrl } from './url-parser';

@Injectable()
export class UrlSerializer {

  constructor(
    @Inject(REFRAMED_OPTIONS) private options: ReframedOptions
  ) {}

  public serialize(url: ParsedUrl): string {
    if (this.options.urlScheme) {
      return serializeUrl(url, this.options.urlScheme);
    } else {
      return serializeUrl(url);
    }
  }

  public deserialize(url: string): ParsedUrl {
    if (this.options.urlScheme) {
      return deserializeUrl(url, this.options.urlScheme);
    } else {
      return deserializeUrl(url);
    }
  }
}
