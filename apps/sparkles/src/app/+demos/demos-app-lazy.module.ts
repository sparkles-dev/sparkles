import { NgModule } from '@angular/core';
import { DemosAppModule } from '@sparkles/demos/app';

/** Lazy-loaded feature module mounting `DemosAppModule` into the sparkles app. */
@NgModule({
  imports: [
    DemosAppModule
  ]
})
export class DemosAppLazyModule {}
