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

/**
 * A resource collection with simple collection metadata.
 *
 * @stable
 */
export interface ResourcesCollection<T> extends ResourceCollection<T, CollectionMetadata> {}
