import { Injectable } from '@angular/core';
import { ParsedUrl } from '../reframed.interfaces';
import { deserializeUrl, serializeUrl } from './url-parser';

@Injectable()
export class UrlSerializer {
  public serialize(url: ParsedUrl): string {
    return serializeUrl(url);
  }

  public deserialize(url: string): ParsedUrl {
    return deserializeUrl(url);
  }
}
