import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Call } from './call';
import { Resource } from './resources';

@Injectable({ providedIn: 'root' })
export class ClientService {

  constructor(
    private http: HttpClient
  ) {}

  /**
   * Get the index page of the API at given `url`
   *
   * @param url URL of index resource, e.g. `/foo/bar/api.json`
   * @return Emits a `Call` object for subsequent API calls
   */
  public index <T> (url: string): Observable<Call<T>> {
    return this.http.get<Resource<T>> (url).pipe(
      map(res => new Call(this.http, res)));
  }

  /**
   * Get a subsequent call from a resource obtained prior.
   *
   * @param resource
   * @return A `Call` object for subsequent API calls
   */
  public call <T> (resource: Resource<T>): Call<T> {
    return new Call(this.http, resource);
  }

}
