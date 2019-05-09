import { Component } from '@angular/core';

@Component({
  selector: 'sp-demos-app',
  template: `
    <ul>
      <li><a [routerLink]="'/button'">Button Demos</a></li>
      <li><a [routerLink]="'/cdk'">CDK Demos</a></li>
    </ul>
    <router-outlet></router-outlet>
  `
})
export class DemosAppComponent {}
