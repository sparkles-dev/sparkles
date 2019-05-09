import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonDemoModule } from './button/button-demo.module';
import { CdkDemoModule } from './cdk/cdk-demo.module';

@NgModule({
  imports: [CommonModule, ButtonDemoModule, CdkDemoModule],
  exports: [ButtonDemoModule, CdkDemoModule]
})
export class ComponentDemosModule {}
