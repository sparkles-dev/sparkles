import { InjectionToken } from '@angular/core';

export type ButtonVariant = 'blue' | 'grey';

export interface ButtonOptions {
  defaultVariant?: ButtonVariant;
  foo?: string;
}

export const BUTTON_DEFAULTS: ButtonOptions = {
  defaultVariant: 'blue'
};

/** @internal */
export function buttonOptionsFactory() {
  return BUTTON_DEFAULTS;
}

/** @internal DI token for ButtonOptions interface */
export const BUTTON_OPTIONS = new InjectionToken<ButtonOptions>('ButtonOptions', {
  providedIn: 'root',
  factory: buttonOptionsFactory
});
