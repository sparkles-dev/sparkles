import { Injectable } from '@angular/core';

/**
 * Injectable token for debugging.
 *
 * #### How To Use
 *
 * Set up a provider in the app module:
 *
 * ```ts
 * import { provideDebug } from '@sparkles/shared';
 * import { environment } from '../environments/environment';
 *
 * @NgModule({
 *   imports: [ .. ],
 *   provider: [
 *     provideDebug(environment)
 *   ]
 * })
 * export class AppModule {}
 * ```
 *
 * Inject the debug token anywhere in the Angular app:
 *
 * ```ts
 * import { Debug } from '@sparkles/shared';
 *
 * @Directive({ ... })
 * export class SharedDirective implements OnInit {
 *
 *   constructor(
 *     private debug: Debug
 *   ) {}
 *
 *   ngOnInit() {
 *     console.log("Is in develop mode?", this.debug.isDevelop);
 *   }
 * }
 * ```
 */
@Injectable({ providedIn: 'root' })
export class Debug {

  public environment: any = {};
  private enabled: string[] = [];

  public get isDevelop(): boolean {
    return this.environment.production !== true;
  }

  public stackTrace(msg?: string) {
    const stackTrace = new Error(msg).stack;
    const firstAt = stackTrace.indexOf('at');
    const secondAt = stackTrace.indexOf('at', firstAt + 1);

    return stackTrace.substring(0, firstAt)
      .concat(stackTrace.substring(secondAt));
  }

  public enable(...tags: string[]) {
    this.enabled = [
      ...this.enabled,
      ...tags
    ].reduce((prev, current) => {
      if (prev.find(item => item === current)) {
        return prev;
      } else {
        return [
          ...prev,
          current
        ];
      }
    }, [] as string[])
  }

  public logger(tag: string) {
    const isEnabled = this.enabled.find(item => item === tag) !== undefined;

    if (isEnabled) {
      // tslint:disable:no-console
      return {
        error: console.error,
        warn: console.warn,
        info: console.info,
        log: console.log,
        debug: console.debug,
        trace: console.trace,
        time: console.time,
        timeEnd: console.timeEnd,
        timeStamp: console.timeStamp,
        group: console.group,
        groupCollapsed: console.groupCollapsed,
        groupEnd: console.groupEnd
      };
      // tslint:enable:no-console
    } else {
      return {
        error: console.error,
        warn: console.warn,
        info: () => {},
        log: () => {},
        debug: () => {},
        trace: () => {},
        time: () => {},
        timeEnd: () => {},
        timeStamp: () => {},
        group: () => {},
        groupCollapsed: () => {},
        groupEnd: () => {}
      };
    }
  }

  public deprecation(what: string, message: string) {
    if (this.isDevelop) {
      console.warn(`[DEPRECATED] ${what}: ${message}`);
    }
  }

  public experimental(what: string, message: string) {
    if (this.isDevelop) {
      // tslint:disable-next-line:no-console
      console.info(`[EXPERIMENTAL] ${what}: ${message}`);
    }
  }

}

/**
 * Sets up the injectable `Debug` token and stores it in a global variable `window.sparkles.debug`.
 *
 * @param environment
 * @see Debug
 */
export function provideDebug(environment: any) {
  const value = new Debug();
  value.environment = environment;

  if (!(window as any).sparkles) {
    (window as any).sparkles = {};
  }
  (window as any).sparkles.debug = value;

  return {
    provide: Debug,
    useValue: value
  };
}
