import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CdkModule } from '@sparkles/components';
// tslint:disable-next-line:nx-enforce-module-boundaries
import { ButtonModule } from '@sparkles/components/button';
import { DomOutletDemoComponent } from './dom-outlet-demo.component';
import { HammerDemoComponent } from './hammer-demo.component';

@NgModule({
  imports: [ CommonModule, CdkModule, ButtonModule ],
  declarations: [ DomOutletDemoComponent, HammerDemoComponent ],
  exports: [ DomOutletDemoComponent, HammerDemoComponent ]
})
export class CdkDemoModule {}
