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
};

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
