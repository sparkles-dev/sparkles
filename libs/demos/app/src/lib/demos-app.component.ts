import { Component } from '@angular/core';

@Component({
  selector: 'sp-demos-app',
  template: `
    <ul>
      <li (click)="activeDemo = 'button'">Button</li>
      <li (click)="activeDemo = 'cdk'">CDK Demos</li>
    </ul>
    <hr />
    <div *ngIf="activeDemo === 'button'">
      <sp-01-button-demo></sp-01-button-demo>
    </div>
    <div *ngIf="activeDemo === 'cdk'">
      <sp-dom-outlet-demo></sp-dom-outlet-demo>
    </div>
    `
})
export class DemosAppComponent {
  activeDemo: string;
}
