import { Directive, Input, Inject } from '@angular/core';
import { BUTTON_OPTIONS, ButtonOptions, ButtonVariant } from './button.options';

@Directive({
  selector: '[spButton]',
  // tslint:disable-next-line:no-host-metadata-property
  host: {
    class: 'btn',
    '[class.btn-blue]': `spButton === 'blue'`,
    '[class.btn-grey]': `spButton === 'grey'`
  }
})
export class ButtonDirective {

  @Input()
  public spButton: ButtonVariant = this.opts.defaultVariant;

  constructor(
    @Inject(BUTTON_OPTIONS) private opts: ButtonOptions
  ) {}

}
