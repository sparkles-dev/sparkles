import { Resource, Link } from './resource.interfaces';

export function hasLinks<T>(value: any): value is Resource<T> {
  return value && value._links;
}

export function isLink(value: any): value is Link {
  return value && typeof value.href === 'string';
}

export function isLinks(value: any): value is Link[] {
  return value instanceof Array && value.every(val => isLink(val));
}

export interface LinkOpts {
  shouldThrow?: boolean;
}

/**
 * Return the link identified by relation `rel` from resource `res`.
 *
 * @param rel Relation name
 * @param res Parent resource
 * @param opts Set `shouldThrow`, if you want an error instead of an `undefined` return value
 * @return An object of type `Link` or an `undefined` value
 * @stable
 */
export function link(rel: string, res: any, opts?: LinkOpts): Link {
  if (hasLinks(res)) {
    const linkForRel = res._links[rel];

    if (isLink(linkForRel)) {
      return linkForRel;
    } else if (opts && opts.shouldThrow) {
      throw new Error(`rel=${rel} is not a Link on Resource ${res}`);
    }
  } else if (opts && opts.shouldThrow) {
    throw new Error(`Object ${res} is not a Resource`);
  }
}

/**
 * Return an array of links identified by relation `rel` from resource `res`.
 *
 * @param rel Relation name
 * @param res Parent resource
 * @param opts Set `shouldThrow`, if you want an error instead of an `undefined` return value
 * @return Array of `Link` or an `undefined` value
 * @stable
 */
export function links(rel: string, res: any, opts?: LinkOpts): Link[] {
  if (hasLinks(res)) {
    const linkForRel = res._links[rel];

    if (isLinks(linkForRel)) {
      return linkForRel;
    } else if (opts && opts.shouldThrow) {
      throw new Error(`rel=${rel} is not an Link[] on Resource ${res}`);
    }
  } else if (opts && opts.shouldThrow) {
    throw new Error(`Object ${res} is not a Resource`);
  }
}
