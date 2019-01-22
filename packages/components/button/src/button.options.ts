import { Injectable } from '@angular/core';

export type ButtonVariant = 'blue' | 'grey';

export interface ButtonOpts {
  defaultVariant: ButtonVariant;

  foo?: string;
}

@Injectable({ providedIn: 'root' })
export class ButtonOptions implements ButtonOpts {

  defaultVariant: ButtonVariant = 'blue';
}
