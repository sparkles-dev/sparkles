import { Component, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'sp-card-header',
  template: `
    <ng-content></ng-content>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CardHeaderComponent {
}
