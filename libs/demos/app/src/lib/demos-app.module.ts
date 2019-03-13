import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DemosAppComponent } from './demos-app.component';

@NgModule({
  imports: [CommonModule],
  declarations: [DemosAppComponent],
  exports: [DemosAppComponent]
})
export class DemosAppModule {}
