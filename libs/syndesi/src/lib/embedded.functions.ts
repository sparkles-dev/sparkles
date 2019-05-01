import { Resource, ResourceWithEmbedded } from './resource.interfaces';
import { isResource, isResources } from './resource.functions';

export function hasEmbedded<T>(value: any): value is ResourceWithEmbedded<T> {
  return value && typeof value._embedded === 'object';
}

export interface EmbeddedOpts<T> {
  guard?: (value: any) => value is T;
  shouldThrow?: boolean;
}

/**
 * Return the embedded resource identified by relation `rel` from parent `res`.
 *
 * @param rel Relation name
 * @param res Parent resource
 * @param opts Set `shouldThrow`, if you want an error instead of an `undefined` return value
 * @return Embedded resource or an `undefined` value
 * @stable
 */
export function embedded<T>(rel: string, res: any, opts?: EmbeddedOpts<T>): Resource<T> {
  if (hasEmbedded(res)) {
    const em = res._embedded[rel];
    const guard = opts && opts.guard ? opts.guard : (v: T): v is T => true;

    if (isResource(em) && guard(em)) {
      return em;
    } else if (opts && opts.shouldThrow) {
      throw new Error(`rel=${rel} is not an embedded Resource on ${res}`);
    }
  } else if (opts && opts.shouldThrow) {
    throw new Error(`${res} has no _embedded resources`);
  }
}

/**
 * Return an array of embedded resources identified by relation `rel` from parent `res`.
 *
 * @param rel Relation name
 * @param res Parent resource
 * @param opts Set `shouldThrow`, if you want an error instead of an `undefined` return value
 * @return Embedded resource or an `undefined` value
 * @stable
 */
export function embeddeds<T>(rel: string, res: any, opts?: EmbeddedOpts<T>): Resource<T>[] {
  if (hasEmbedded(res)) {
    const em = res._embedded[rel];
    const guard = opts && opts.guard ? opts.guard : (v: T): v is T => true;

    const isArrayOfT = (value: any): value is T[] => {
      return value instanceof Array && value.every(val => guard(val));
    };

    if (isResources<T>(em) && isArrayOfT(em)) {
      return em;
    } else if (opts && opts.shouldThrow) {
      throw new Error(`rel=${rel} is not an embedded Resource[] on ${res}`);
    }
  } else if (opts && opts.shouldThrow) {
    throw new Error(`${res} has no _embedded resources`);
  }
}
