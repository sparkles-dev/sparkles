import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OverlayModule } from '@angular/cdk/overlay';
import { PortalModule } from '@angular/cdk/portal';
import { DomOutletComponent } from './dom-outlet/dom-outlet.component';
import { TemplateDirective } from './template/template.directive';

@NgModule({
  imports: [CommonModule, OverlayModule, PortalModule],
  declarations: [TemplateDirective, DomOutletComponent],
  exports: [TemplateDirective, DomOutletComponent]
})
export class CdkModule {}
