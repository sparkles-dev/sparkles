import { Injectable } from '@angular/core';

export type ButtonVariant = 'blue' | 'grey';

export interface ButtonOptions {
  defaultVariant: ButtonVariant;
  foo?: string;
}

export const BUTTON_DEFAULTS: ButtonOptions = {
  defaultVariant: 'blue'
};

/** @internal */
export function buttonOptionsFactory() {
  const o = new ButtonOptionsToken();
  Object.assign(o, BUTTON_DEFAULTS);
  
  return o;
}

/** @internal DI token for ButtonOptions interface */
@Injectable({
  providedIn: 'root',
  useFactory: buttonOptionsFactory
})
export class ButtonOptionsToken implements ButtonOptions {
  defaultVariant;
}
