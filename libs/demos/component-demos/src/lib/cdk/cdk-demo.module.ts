import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CdkModule } from '@sparkles/components';
import { ButtonModule } from '@sparkles/components/button';
import { DomOutletDemoComponent } from './dom-outlet-demo.component';

@NgModule({
  imports: [ CommonModule, CdkModule, ButtonModule ],
  declarations: [ DomOutletDemoComponent ],
  exports: [ DomOutletDemoComponent]
})
export class CdkDemoModule {}
