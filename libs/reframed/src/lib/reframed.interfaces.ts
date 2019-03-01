import { Type, InjectionToken, ValueProvider, ANALYZE_FOR_ENTRY_COMPONENTS } from '@angular/core';

/**
 * Marker interface that a component for an entry point should implement.
 *
 * @stable
 */
export interface AppLaunch {
  onAppLaunch(url: ParsedUrl);
}

export function isAppLaunch(value: any): value is AppLaunch {
  return value !== undefined && typeof value.onAppLaunch === 'function';
}

export interface ParsedUrl {
  url: string;
  appName: string;
  entryPoint: string;
  params?: {
    [key: string]: any;
  };
}

export interface Entry {
  path: string;
  component: Type<any>;
}

export const ENTRIES = new InjectionToken<Entry[]>('@sparkles/reframed: Entry[]');

export function provideEntries(entries: Entry[]): ValueProvider[] {
  return [
    {
      provide: ANALYZE_FOR_ENTRY_COMPONENTS,
      multi: true,
      useValue: entries
    },
    {
      provide: ENTRIES,
      useValue: entries
    }
  ];
}

/**
 * Global configuration options for the reframed module.
 */
export interface ReframedOptions {

  /**
   * Prefix for constructing iframe urls.
   */
  pathPrefix?: string;

  /**
   * Optional, th URL scheme for parsing launcher urls. Example: `myapps://`
   */
  urlScheme?: string;
}

export const REFRAMED_OPTIONS = new InjectionToken<ReframedOptions>('@sparkles/reframed: ReframedOptions');

export function provideReframedOptions(options: ReframedOptions): ValueProvider {
  return {
    provide: REFRAMED_OPTIONS,
    useValue: options
  };
}

export const DEFAULT_REFRAMED_OPTIONS: ReframedOptions = {
  pathPrefix: '/'
};
