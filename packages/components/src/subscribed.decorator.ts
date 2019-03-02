/**
 * Automatically unsubscribes from a RxJS subscription in `ngOnDestroy()`.
 *
 * ##### How To Use
 *
 * ```ts
 * import { Subscribed } from '@sparkles/components';
 *
 * export class MyComponent {
 *
 *   @Subscribed()
 *   private sub = this.store.select(selectState)
 *     .subscribe(next => { this.storeValue = next; })
 *
 *   @Subscribed()
 *   private sub2;
 *
 *   ngAfterContentInit() {
 *     this.sub2 = this.myForm.valueChanges.subscribe(..);
 *   }
 * }
 * ```
 *
 * ##### How It Works
 *
 * The decorator registers an `ngOnDestroy()` lifecycle hook that unsubscribes.
 *
 * @link https://netbasal.com/automagically-unsubscribe-in-angular-4487e9853a88
 * @decorator
 * @stable
 */
export function Subscribed() {
  return function(target, propertyKey) {
    // Grab reference to toriginal `ngOnDestroy()` hook
    const original = target.constructor.prototype.ngOnDestroy;

    target.constructor.prototype.ngOnDestroy = function() {
      // Try to unsubscribe
      const prop = this[propertyKey];
      if (prop && typeof prop.unsubscribe === 'function') {
        prop.unsubscribe();
      }

      // Call the original `ngOnDestroy()` hook
      if (original && typeof original === 'function') {
        original.apply(this, arguments);
      }
    };
  };
}
