import { Directive, Input } from '@angular/core';
import { ButtonOptionsToken, ButtonVariant } from './button.options';

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
    private opts: ButtonOptionsToken
  ) {}

}
