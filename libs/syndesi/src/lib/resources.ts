/**
 * Link representation.
 *
 * @experimental
 */
export interface Link {
  href: string;
  templated?: boolean;
}

/**
 * Base type for resource representations.
 *
 * A resource must be identifiable by a `self` URI.
 *
 * The generic type parameter may be used to extend existing type information, thus allows
 * to add resource-capabilities to existing code in an unobstrusive way.
 *
 * ### How To Use
 *
 * The first approach is to declare a value with generic type information:
 *
 * ```ts
 * export interface User {
 *   id: number;
 *   name: string;
 * }
 *
 * const foo: Resource<User> = {
 *   _links: {
 *     self: { href: '/user/123' }
 *   },
 *   id: 123,
 *   name: 'Theo Test'
 * }
 * ```
 *
 * The second approach is to extend existing type declarations:
 *
 * ```ts
 * export interface User {
 *   id: number;
 *   name: string;
 * }
 *
 * export interface UserResource extends Resource<User> {
 * }
 *
 * const foo: UserResource = {
 *   _links: {
 *     self: { href: '/user/123' }
 *   },
 *   id: 123,
 *   name: 'Theo Test'
 * }
 * ```
 *
 * @stable
 */
export type Resource<T> = T & {
  _links: {
    self: Link;
    [key: string]: Link | Link[];
  };
  _embedded?: {
    [key: string]: Resource<any> | Resource<any>[];
  };
}

/**
 * A resource with `_embedded` resources.
 *
 * @stable
 */
export type ResourceWithEmbedded<T> = Resource<T> & {
  _embedded: {
    [key: string]: Resource<any> | Resource<any>[];
  }
};

/**
 * Resource metadata for collections of resources.
 *
 * @stable
 */
export interface CollectionMetadata {
  /** The number of items contained in this collection representation. */
  count: number;
  /** The total count of items in the full collection. Optional. */
  totalCount?: number;
  /** The index of this page. Optional, but required when paging is enabled. */
  page?: number;
  /** The total count of pages. Optional, but required when paging is enabled. */
  totalPages?: number;
  /** The number of items per each page. Optional, but required when paging is enabled. */
  pageSize?: number;
}

/**
 * A collection of resources.
 *
 * The embedded `content` property must be an array of resources.
 * Each resource must be indentifiable by a `self` URI.
 *
 * `ResourceCollection` itself is a resource and must be identifiable by `self` URI.
 * It has metadata information from `CollectionMetadata`, such as the amount of items or paging
 * information.
 *
 * @stable
 */
export type ResourceCollection<T, C extends CollectionMetadata> = Resource<C> & {
  _embedded: {
    content: Resource<T>[];
    [key: string]: any;
  }
}

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

export function hasEmbedded<T>(value: any): value is ResourceWithEmbedded<T> {
  return value && typeof value._embedded === 'object';
}

export function isResource<T>(value: any): value is Resource<T> {
  return hasLinks(value) && isLink(value._links.self);
}

export function isResources<T>(value: any): value is Resource<T>[] {
  return value instanceof Array && value.every(val => isResource(val));
}

export interface EmbeddedOpts<T> {
  guard?: (value: any) => value is T;
  shouldThrow?: boolean;
};

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
