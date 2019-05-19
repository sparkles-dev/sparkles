import { NgModule, Optional, SkipSelf } from '@angular/core';
import { RouterModule } from '@angular/router';

/*
const ROUTES: Routes = [ ... ];
const CONFIG: ExtraOptions = { ... };

RouterModule.forRoot(ROUTES, CONFIG)
*/

/**
 * Router configuration for the sparkles app.
 * It provides the root router configuration with the top-level routes.
 *
 * The routes refer
 *  - either to a library component (sync loading, main bundle),
 *  - or to a lazy feature module (lazy loading, feature bundle).
 *
 * With sync loading of routes, the library component will be bundled into the app's main bundle.
 * With a lazy-loaded feature module, the library component will be bundled into the feature's bundle.
 */
@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: '',
          loadChildren: './+demos/demos-app-lazy.module#DemosAppLazyModule'
        }
      ],
      {
        useHash: true,
        enableTracing: false
      }
    )
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
  constructor(@Optional() @SkipSelf() parentModule: AppRoutingModule) {
    if (parentModule) {
      throw new Error(
        `${
          AppRoutingModule.name
        } is already loaded. Import it in the AppModule only!`
      );
    }
  }
}
