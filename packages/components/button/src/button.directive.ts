import { Directive, Input } from '@angular/core';

export type ButtonVariant = 'blue' | 'grey';

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
  public sparklesButton: ButtonVariant = 'blue';

}
