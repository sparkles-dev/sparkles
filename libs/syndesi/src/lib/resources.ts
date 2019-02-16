export interface Link {
  href: string;
  templated?: boolean;
}

export type Resource<T> = T & {
  _links: {
    self: Link;
    [key: string]: Link | Link[];
  };
  _embedded?: {
    [key: string]: Resource<any> | Resource<any>[];
  };
}

export type ResourceWithEmbedded<T> = Resource<T> & {
  _embedded: {
    [key: string]: Resource<any> | Resource<any>[];
  }
};

export interface ResourceCollection<T> {
  _embedded: {
    content: Resource<T>[];
    [key: string]: any;
  }
}

export type ResourcesCollection<T, C> = Resource<T> & {
  _embedded: {
    content: Resource<C>[];
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

export function link(rel: string, res: any, shouldThrow?: boolean): Link {
  if (hasLinks(res)) {
    const link = res._links[rel];

    if (isLink(link)) {
      return link;
    } else if (shouldThrow) {
      throw new Error(`rel=${rel} is not a Link on Resource ${res}`);
    }
  } else if (shouldThrow) {
    throw new Error(`Object ${res} is not a Resource`);
  }
}

export function links(rel: string, res: any, shouldThrow?: boolean): Link[] {
  if (hasLinks(res)) {
    const link = res._links[rel];

    if (isLinks(link)) {
      return link;
    } else if (shouldThrow) {
      throw new Error(`rel=${rel} is not an Link[] on Resource ${res}`);
    }
  } else if (shouldThrow) {
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

export type EmbeddedOpts<T> = {
  guard?: (value: any) => value is T;
  shouldThrow?: boolean;
};

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
