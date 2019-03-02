import { Type } from '@angular/core';
import { TestBed, ComponentFixture } from '@angular/core/testing';

/**
 * Creates a `ComponentFixture` for the given component class, optionally overwrites the HTML template.
 *
 * @param type Component class
 * @param template HTML template string
 * @returns A fixture for `type`
 *
 * @stable
 */
export function createTestComponent<T>(
  type: Type<T>,
  template?: string
): ComponentFixture<T> {
  if (template) {
    return TestBed.overrideComponent(type, {
      set: { template }
    }).createComponent(type);
  } else {
    return TestBed.createComponent(type);
  }
}
