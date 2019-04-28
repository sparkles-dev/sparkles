import { Component } from '@angular/core';

@Component({
  selector: 'sp-demos-app',
  template: `
    <ul>
      <li (click)="activeDemo = 'button'">Button</li>
    </ul>
    <hr />
    <div *ngIf="activeDemo === 'button'">
      <sp-01-button-demo></sp-01-button-demo>
    </div>
  `
})
export class DemosAppComponent {
  activeDemo: string;
}
