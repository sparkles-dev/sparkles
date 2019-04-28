import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonDemoModule } from './button/button-demo.module';

@NgModule({
  imports: [CommonModule, ButtonDemoModule],
  exports: [ButtonDemoModule]
})
export class ComponentDemosModule {}
