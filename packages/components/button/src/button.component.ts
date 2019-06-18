import { Component, Input } from '@angular/core';
import { ButtonVariant } from './button.options';

@Component({
  selector: 'sp-button',
  template: `<ng-content></ng-content>`,
  // tslint:disable-next-line:no-host-metadata-property
  host: {
    class: 'btn',
    '[class.btn-blue]': `spButton === 'blue'`,
    '[class.btn-grey]': `spButton === 'grey'`
  }
})
export class ButtonComponent {

  @Input()
  public spButton: ButtonVariant = 'blue';

}
