import { Type, InjectionToken, ValueProvider, ANALYZE_FOR_ENTRY_COMPONENTS } from '@angular/core';

export interface AppLaunch {
  onAppLaunch(url: ParsedUrl);
}

export interface ParsedUrl {
  url: string;
  appName: string;
  entryPoint: string;
  params?: {
    [key: string]: any;
  };
}

export interface EntryPoint {
  path: string;
  component: Type<any>;
}

export interface AppResolverOptions {
  prefix: string;
}

export function isAppLaunch(value: any): value is AppLaunch {
  return value !== undefined && typeof value.onAppLaunch === 'function';
}

export const ENTRY_POINTS = new InjectionToken<EntryPoint[]>('u: app launcher entry points');

export function provideEntries(entries: EntryPoint[]): ValueProvider[] {
  return [
    {
      provide: ANALYZE_FOR_ENTRY_COMPONENTS,
      multi: true,
      useValue: entries
    },
    {
      provide: ENTRY_POINTS,
      useValue: entries
    }
  ];
}

export const URL_RESOLVER_OPTIONS = new InjectionToken<AppResolverOptions>('u: app launcher resolver options');

export function provideUrlResolverOptions(options: AppResolverOptions): ValueProvider {
  return {
    provide: URL_RESOLVER_OPTIONS,
    useValue: options
  };
}

export const DEFAULT_URL_RESOLVER_OPTIONS: AppResolverOptions = {
  prefix: '/'
};
