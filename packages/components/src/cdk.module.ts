import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TemplateDirective } from './template/template.directive';

@NgModule({
  imports: [CommonModule],
  declarations: [TemplateDirective],
  exports: [TemplateDirective]
})
export class CdkModule {}
