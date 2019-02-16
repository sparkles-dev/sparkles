import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Call } from './call';
import { Resource } from './resources';

@Injectable({ providedIn: 'root' })
export class HalClient {

  constructor(
    private http: HttpClient
  ) {}

  /**
   * Get the index page of the API at given `url`
   *
   * @param url URL of index resource, e.g. `/foo/bar/api.json`
   */
  public index <T extends Resource> (url: string): Observable<Call<T>> {
    return this.http.get<T> (url).pipe(
      map(res => new Call(this.http, res)));
  }

  public call <T extends Resource> (resource: T): Call<T> {
    return new Call(this.http, resource);
  }

}
