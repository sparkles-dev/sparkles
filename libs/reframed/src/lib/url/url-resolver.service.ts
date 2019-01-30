import { Injectable, Inject } from '@angular/core';
import { ParsedUrl, AppResolverOptions, URL_RESOLVER_OPTIONS } from '../app-launcher.interfaces';

@Injectable()
export class UrlResolver {
  constructor(@Inject(URL_RESOLVER_OPTIONS) private options: AppResolverOptions) {}

  public resolvePublicUrl(url: ParsedUrl) {
    let value = this.options.prefix.concat('u/', url.appName, '/');

    if (url.params) {
      // TODO: append params also to the url query param
      // this is especially needed for the ?locale param which is read at app start
      // also, some apps take patientId or medicalCase as query params before the hash segment
      // we need to append those params here and also handle them in the external entry component

      // Later, params will also postMesssage()'d to the iframe
    }

    if (url.entryPoint) {
      value = value.concat('#/external/', url.entryPoint);
    }

    return value;
  }
}
