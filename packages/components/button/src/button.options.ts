import { Injectable } from '@angular/core';

export type ButtonVariant = 'blue' | 'grey';

@Injectable({ providedIn: 'root' })
export class ButtonOptions {

  defaultVariant: ButtonVariant = 'blue';

  foo?: string;
}
