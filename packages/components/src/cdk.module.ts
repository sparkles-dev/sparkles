import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OverlayModule } from '@angular/cdk/overlay';
import { PortalModule } from '@angular/cdk/portal';
import { DomOutletComponent } from './dom-outlet/dom-outlet.component';
import { FullScreenDirective } from './full-screen/full-screen.directive';
import { TemplateDirective } from './template/template.directive';
import { HammerDirective } from './touch-events/hammer.directive';

/**
 * A component development kit that offers flexible utilities and widgets.
 *
 * @stable
 */
@NgModule({
  imports: [CommonModule, OverlayModule, PortalModule],
  declarations: [TemplateDirective, DomOutletComponent, FullScreenDirective, HammerDirective],
  exports: [TemplateDirective, DomOutletComponent, FullScreenDirective, HammerDirective]
})
export class CdkModule {}
