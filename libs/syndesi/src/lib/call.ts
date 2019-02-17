import { HttpClient, HttpResponse } from '@angular/common/http';
import { Resource } from './resources';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { expand, UriParams } from './uri';

const nextCall = <S, T>(call: Call<S>) => {

  return function (res: Resource<T> | HttpResponse<Resource<T>>) {
    const json = res instanceof HttpResponse ? res.body : res;
    const response = res instanceof HttpResponse  ? res : undefined;

    return new Call<T>(call['_http'], json, response);
  };
};

export class Call<T> {
  private _http: HttpClient;
  private _uri: string;
  private _method: string;
  private _options = {};

  constructor(
    http: HttpClient | Call<any>,
    public resource: Resource<T>,
    public response?: HttpResponse<T>
  ) {
    if (http instanceof HttpClient) {
      this._http = http;
    } else {
      this._http = http._http;
    }
  }

  public delete(rel: string, params?: UriParams, body?: any): Call<T> {
    this.uri(rel, params);
    this.method('delete');

    return this;
  }

  public get(rel: string, params?: UriParams): Call<T> {
    this.uri(rel, params);
    this.method('get');

    return this;
  }

  public head(rel: string, params?: UriParams): Call<T> {
    this.uri(rel, params);
    this.method('head');

    return this;
  }

  public options(rel: string, params?: UriParams): Call<T> {
    this.uri(rel, params);
    this.method('options');

    return this;
  }

  public patch(rel: string, params?: UriParams): Call<T> {
    this.uri(rel, params);
    this.method('patch');

    return this;
  }

  public post(rel: string, params?: UriParams, body?: any): Call<T> {
    this.uri(rel, params);
    this.method('post');
    this.body(body);

    return this;
  }

  public put(rel: string, params?: UriParams, body?: any): Call<T> {
    this.uri(rel, params);
    this.method('put');
    this.body(body);

    return this;
  }

  public uri(rel: string, params?: UriParams): Call<T> {
    this._uri = this.expandLink(rel, params);

    return this;
  }

  public method(verb: string): Call<T> {
    this._method = verb;

    return this;
  }

  public opts(opts: any): Call<T> {
    this._options = opts;

    return this;
  }

  public body(body: any): Call<T> {
    this._options = {
      ...this._options,
      body
    };

    return this;
  }

  public send<R>(): Observable<Call<R>> {
    if (!this._method) {
      throw new Error('method is a required parameter! Please set it with method() or one of the short-hands like get().');
    }
    if (!this._uri) {
      throw new Error('uri is a required parameter! Please set it with uri() or one of the short-hands like get().');
    }

    return this._http.request<Resource<R>>(
        this._method,
        this._uri,
        {
          ...this._options,
          observe: 'response',
          responseType: 'json'
        }
      ).pipe(map(nextCall(this)));
  }

  private expandLink(rel: string, params?: UriParams): string {
    const link = this.resource._links[rel];

    if (!link) {
      throw new Error(`Link with rel=${rel} does not exist`);
    }

    if (link instanceof Array) {
      throw new Error('Traversing arrays not implemented');
    } else {
      return expand(link, params);
    }
  }
}
