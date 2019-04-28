import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ComponentDemosModule } from '@sparkles/demos/component-demos';
import { DemosAppComponent } from './demos-app.component';

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
        component: DemosAppComponent
      }
    ]),
    ComponentDemosModule
  ],
  declarations: [DemosAppComponent]
})
export class DemosAppModule {}
