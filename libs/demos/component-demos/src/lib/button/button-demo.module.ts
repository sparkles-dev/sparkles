import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
// tslint:disable-next-line:nx-enforce-module-boundaries
import { ButtonModule } from '@sparkles/components/button';
import { ButtonDemo01Component } from "./01-button-demo.component";

const DEMOS = [ ButtonDemo01Component ];

@NgModule({
  imports: [ CommonModule, ButtonModule ],
  declarations: [ ...DEMOS ],
  exports: [ ...DEMOS ]
})
export class ButtonDemoModule {}
