import { Directive, Input, Inject } from '@angular/core';
import { BUTTON_OPTIONS, ButtonOptions, ButtonVariant } from './button.options';

@Directive({
  selector: '[sparklesButton]',
  host: {
    class: 'btn',
    '[class.btn-blue]': `sparklesVariant === 'blue'`,
    '[class.btn-grey]': `sparklesVariant === 'grey'`
  }
})
export class ButtonDirective {

  @Input()
  public sparklesButton: ButtonVariant = this.opts.defaultVariant;

  constructor(
    @Inject(BUTTON_OPTIONS) private opts: ButtonOptions
  ) {}

}
