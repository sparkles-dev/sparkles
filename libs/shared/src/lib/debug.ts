import { Injectable, InjectionToken, Inject, Optional } from '@angular/core';
import { unique } from './functional';

export const ENVIRONMENT = new InjectionToken<any>(
  '@sparkles/shared: ENVIRONMENT'
);

function attachToGlobal(key: string, value: any) {
  if (!(window as any).sparkles) {
    (window as any).sparkles = {};
  }

  (window as any).sparkles[key] = value;
}

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
 *
 * #### How It Works
 *
 * Stores an instance of this class in a global variable `window.sparkles.debug`.
 */
@Injectable({ providedIn: 'root' })
export class Debug {
  public environment: any = {};

  private enabled: string[] = [];

  constructor(@Inject(ENVIRONMENT) @Optional() environment?: any) {
    if (environment) {
      this.environment = environment;
    }

    attachToGlobal('debug', this);
  }

  public get isDevelop(): boolean {
    return this.environment.production !== true;
  }

  public stackTrace(msg?: string) {
    const stackTrace = new Error(msg).stack;
    const firstAt = stackTrace.indexOf('at');
    const secondAt = stackTrace.indexOf('at', firstAt + 1);

    return stackTrace
      .substring(0, firstAt)
      .concat(stackTrace.substring(secondAt));
  }

  public enable(...tags: string[]) {
    this.enabled = unique([...this.enabled, ...tags]);
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
 * Sets up the injectable `Debug` token.
 *
 * @param environment
 * @see Debug
 */
export function provideDebug(environment: any) {
  return {
    provide: ENVIRONMENT,
    useValue: environment
  };
}
