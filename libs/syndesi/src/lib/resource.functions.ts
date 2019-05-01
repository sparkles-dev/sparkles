import { Resource } from './resource.interfaces';
import { hasLinks, isLink } from './link.functions';

export function isResource<T>(value: any): value is Resource<T> {
  return hasLinks(value) && isLink(value._links.self);
}

export function isResources<T>(value: any): value is Resource<T>[] {
  return value instanceof Array && value.every(val => isResource(val));
}
