import { QueryList } from '@angular/core';
import { Subscription } from 'rxjs';

/**
 * Component classes annotated with `@DynamicQueries()` receive lifecycle hooks for properties
 * annotated with `@ContentChildren()` and `@ViewChildren()`.
 *
 * ##### How It Works
 *
 * The decorator automatically subscribes to the `changes` observable of `QueryList`.
 *
 * ##### How To Use
 *
 * The component class must be annotated with `@DynamicQueries()`.
 * To receive a lifecycle hook when `@ContentChildren()` changes, the class should implement the
 * interface `ContentChanges`.
 * To receive a lifecycle hook when `@ViewChildren()` changes, the class should implement the
 * interface `ViewChanges`.
 *
 * Here is an example for listening to content changes:
 *
 * ```ts
 * @Component({
 *   template: `<ng-content></ng-content>`
 * })
 * @DynamicQueries()
 * export class Component implements ContentChanges {
 *   @ContentChildren('foo')
 *   public dynamicContent: QueryList<any>;
 *
 *   spOnContentChanges(change: QueryListChange) { // <-- Called when the projected content changes
 *     const value = change['dynamicContent']; // <-- represents property `dynamicContent`
 *   }
 * }
 * ```
 *
 * Here is an example for listening to changes of view children:
 *
 * ```ts
 * @Component({
 *   template: `<foo *ngFor="let val of [1,2,3]></foo>`
 * })
 * @DynamicQueries()
 * export class Component implements ViewChanges {
 *   @ViewChildren('foo')
 *   public dynamicViews: QueryList<any>;
 *
 *   spOnViewChanges(change: QueryListChange) { // <-- Called when the view children change
 *     const value = change['dynamicViews']; // <-- represents property `dynamicViews`
 *   }
 * }
 * ```
 *
 * @stable
 */
export function DynamicQueries() {

  return function (target: any) {

    // Grab reference to toriginal `ngAfterContentInit()` hook
    const originalAfterContentInit = target.prototype.ngAfterContentInit;
    const originalAfterViewInit = target.prototype.ngAfterViewInit;
    const originalOnDestroy = target.prototype.ngOnDestroy;
    const subscriptions: Subscription[] = [];

    // Sets up the synchronization hook for dynamic content
    target.prototype.ngAfterContentInit = function () {
      // Grab properties of type QueryList
      for (const propertyKey in this) {
        if (this.hasOwnProperty(propertyKey)) {
          const property = this[propertyKey];
          if (property instanceof QueryList) {
            // Subscribe to content changes
            const sub = property.changes.subscribe(() => {
              notifyOnContentChanges(this, propertyKey, property);
            });

            subscriptions.push(sub);
            notifyOnContentChanges(this, propertyKey, property);
          }
        }
      }

      // Call the original `ngAfterContentInit()` hook
      if (originalAfterContentInit && typeof originalAfterContentInit === 'function') {
        originalAfterContentInit.apply(this, arguments);
      }
    };

    // Sets up the synchronization hook for dynamic views
    target.prototype.ngAfterViewInit = function () {
      // Grab properties of type QueryList
      for (const propertyKey in this) {
        if (this.hasOwnProperty(propertyKey)) {
          const property = this[propertyKey];
          if (property instanceof QueryList) {
            // Subscribe to content changes
            const sub = property.changes.subscribe(() => {
              notifyOnViewChanges(this, propertyKey, property);
            });

            subscriptions.push(sub);
            notifyOnViewChanges(this, propertyKey, property);
          }
        }
      }

      // Call the original `ngAfterViewInit()` hook
      if (originalAfterViewInit && typeof originalAfterViewInit === 'function') {
        originalAfterViewInit.apply(this, arguments);
      }
    };


    // Cleans up automatic subscriptions
    target.prototype.ngOnDestroy = function () {
      subscriptions.forEach(sub => sub.unsubscribe());

      // Call the original `ngOnDestroy()` hook
      if (originalOnDestroy && typeof originalOnDestroy === 'function') {
        originalOnDestroy.apply(this, arguments);
      }
    };
  };
}

function notifyOnContentChanges(target: any, propertyKey: string, queryList: QueryList<any>) {
  if (isContentChanges(target)) {
    const change = {};
    change[propertyKey] = queryList;
    target.spOnContentChanges(change);
  }
}

function isContentChanges(value: any): value is ContentChanges {
  return value && typeof value.spOnContentChanges === 'function';
}

function notifyOnViewChanges(target: any, propertyKey: string, queryList: QueryList<any>) {
  if (isViewChanges(target)) {
    const change = {};
    change[propertyKey] = queryList;
    target.spOnViewChanges(change);
  }
}

function isViewChanges(value: any): value is ViewChanges {
  return value && typeof value.spOnViewChanges === 'function';
}

/**
 * A change reported to a lifecycle hook when a `QueryList` changes.
 *
 * See `DynamicQueries()` for more details.
 *
 * @stable
 */
export interface QueryListChange {
  [key: string]: QueryList<any>;
}

/**
 * Custom lifecycle hook that is invoked when a `QueryList` property, annotated with `@ContentChildren()`,
 * of the parent component receives changes from dynamic content.
 *
 * See `DynamicQueries()` for more details.
 *
 * @stable
 */
export interface ContentChanges {
  spOnContentChanges(change: QueryListChange): void;
}

/**
 * Custom lifecycle hook that is invoked when a `QueryList` property, annotated with `@ViewChildren()`,
 * of the parent component receives changes from dynamic content.
 *
 * See `DynamicQueries()` for more details.
 *
 * @stable
 */
export interface ViewChanges {
  spOnViewChanges(change: QueryListChange): void;
}
