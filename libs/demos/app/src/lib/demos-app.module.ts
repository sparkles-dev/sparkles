import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ComponentDemosModule } from './component-demos.module';
import { DemosAppComponent } from './demos-app.component';
import { ButtonDemo01Component } from './button/01-button-demo.component';
import { DomOutletDemoComponent } from './cdk/dom-outlet-demo.component';
import { HammerDemoComponent } from './cdk/hammer-demo.component';

/**
 * Feature module for the demos application.
 *
 * It is implemented with child configuration(s) only, thus can be mounted into any app shell.
 */
@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '',
        component: DemosAppComponent,
        children: [
          {
            path: 'button',
            component: ButtonDemo01Component
          },
          {
            path: 'cdk/dom-outlet',
            component: DomOutletDemoComponent
          },
          {
            path: 'cdk/hammerjs',
            component: HammerDemoComponent
          }
        ]
      }
    ]),
    ComponentDemosModule
  ],
  declarations: [DemosAppComponent]
})
export class DemosAppModule {}
