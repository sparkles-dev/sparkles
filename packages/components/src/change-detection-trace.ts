import { ApplicationRef, Provider, isDevMode } from '@angular/core';

export class TracingApplicationRef extends ApplicationRef {

  tick() {
    if (isDevMode()) {
      // tslint:disable-next-line:no-console
      console.log('%cAngular is running change detection...', 'color: orange; font-style: italic');
      // tslint:disable-next-line:no-console
      console.time('ApplicationRef#tick()');
      super.tick();
      // tslint:disable-next-line:no-console
      console.timeEnd('ApplicationRef#tick()');
    } else {
      super.tick();
    }
  }
}

/**
 * Sets up tracing of change detection cycles in the browser console.
 *
 * ##### How To Use
 *
 * Import in the app root module:
 *
 * ```ts
 * import { traceChangeDetection } from '@sparkles/components';
 *
 * @NgModule({
 *   providers: [ traceChangeDetection() ]
 * })
 * export class AppModule {}
 * ```
 *
 * ##### How It Works
 *
 * It overrides the provider for Angular's built-in `ApplicationRef` service and intercepts calls
 * to `tick()`.
 *
 * @link https://google.github.io/tracing-framework/
 * @link https://github.com/angular/angular/blob/master/packages/core/src/application_ref.ts
 * @link https://github.com/angular/angular/blob/master/packages/core/src/profile/wtf_impl.ts
 */
export function traceChangeDetection(): Provider {

  return {
    provide: ApplicationRef,
    useClass: TracingApplicationRef
  };
}
